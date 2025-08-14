package com.ilkan.service;

import com.ilkan.domain.enums.Role;
import com.ilkan.exception.ReservationExceptions;
import com.ilkan.domain.entity.ReservationDay;
import com.ilkan.domain.entity.Building;
import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.entity.User;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.reservationdto.CreateReservationReqDto;
import com.ilkan.dto.reservationdto.OccupiedDayResDto;
import com.ilkan.dto.reservationdto.ReservationResDto;
import com.ilkan.repository.BuildingRepository;
import com.ilkan.repository.ReservationDayRepository;
import com.ilkan.repository.ReservationRepository;
import com.ilkan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final BuildingRepository buildingRepo;
    private final UserRepository userRepo;
    private final ReservationRepository reservationRepo;
    private final ReservationDayRepository dayRepo;

    /**
     * 새로운 예약 생성
     *
     * - 요청에서 전달받은 체크인/체크아웃 날짜 검증
     * - 예약 대상 건물과 수행자를 조회.
     * - 해당 기간에 건물이 이미 점유되어 있는 경우 사전 점유 여부를 확인하여 예외 발생
     * - 예약 정보를 저장하고, 체크인일부터 체크아웃 전날까지의 날짜를 {@code ReservationDay} 엔티티로 생성하여 저장
     *
     * @param req 예약 생성 요청 DTO
     * @param role 예약 생성 요청자 역할
     *
     * @return 생성된 {@link Reservation} 정보를 담은 {@link ReservationResDto}.
     *
     * @throws com.ilkan.exception.ReservationExceptions.InvalidRange
     *         체크인 또는 체크아웃 날짜가 {@code null}이거나, 체크아웃 날짜가 체크인 날짜와 같거나 이전인 경우
     * @throws com.ilkan.exception.ReservationExceptions.BuildingNotFound
     *         {@code buildingId}에 해당하는 건물이 존재하지 않는 경우
     * @throws com.ilkan.exception.ReservationExceptions.PerformerNotFound
     *         {@code performerId}에 해당하는 사용자가 존재하지 않는 경우.
     * @throws com.ilkan.exception.ReservationExceptions.AlreadyOccupied
     *         지정한 기간에 건물이 이미 예약되어 있는 경우
     *
     */
    public ReservationResDto create(CreateReservationReqDto req, Role role) {

        if (req.getBuildingId() == null) {
            throw new ReservationExceptions.BuildingNotFound();
        }
        validateRange(req.getCheckInDate(), req.getCheckOutDate());

        Building building = buildingRepo.findForUpdate(req.getBuildingId())
                .orElseThrow(ReservationExceptions.BuildingNotFound::new);

        // role로 수행자 꺼내오기
        if (role != Role.PERFORMER) {
                throw new ReservationExceptions.Forbidden();
        }
        User performer = userRepo.findFirstByRole(Role.PERFORMER)
                .orElseThrow(ReservationExceptions.PerformerNotFound::new);


        // 1. 빠른 check
        boolean occupied = dayRepo.existsByBuildingIdAndDayBetween(
                req.getBuildingId(),
                req.getCheckInDate(),
                req.getCheckOutDate().minusDays(1) // [in, out) 규칙이면 out-1까지 체크
        );
        if (occupied) throw new ReservationExceptions.AlreadyOccupied();

        // 2. 예약 생성
        Reservation reservation = req.toEntity(performer, building);
        reservationRepo.save(reservation);

        // 3. reservationDay 벌크 저장
        List<ReservationDay> days = new java.util.ArrayList<>();
        for (LocalDate d = req.getCheckInDate(); d.isBefore(req.getCheckOutDate()); d = d.plusDays(1)) {
            days.add(ReservationDay.builder()
                    .reservationId(reservation.getId())
                    .buildingId(building.getId())
                    .day(d)
                    .build());
        }
        try {
            dayRepo.saveAll(days);
        } catch (DataIntegrityViolationException e) {
            throw new ReservationExceptions.AlreadyOccupied();
        }

        return ReservationResDto.fromEntity(reservation);
    }

    /**
     * 예약 날짜 범위 점검
     *
     * @param from 시작 날짜 (포함)
     * @param to 종료 날짜 (미포함)
     *
     * @throws com.ilkan.exception.ReservationExceptions.InvalidRange
     *         날짜가 {@code null}이거나, 시작일이 종료일과 같거나 이후이거나,
     *         시작일로부터 6개월을 초과하는 범위인 경우.
     */
    private void validateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || !from.isBefore(to)) {
            throw new ReservationExceptions.InvalidRange("invalid range");
        }
        if (from.plusMonths(6).isBefore(to)) { // 최대 6개월 같은 가드
            throw new ReservationExceptions.InvalidRange("최대 6개월 범위만 지정할 수 있습니다.");
        }
    }

    /**
     * 지정 건물의 점유 일자 목록
     *
     * @param buildingId 점유 일자를 조회할 건물의 ID
     * @param from 조회 시작 날짜
     * @param to 조회 종료 날짜
     * @return 점유 일자 목록을 담은 {@link OccupiedDayResDto} 리스트
     *
     *  @throws com.ilkan.exception.ReservationExceptions.InvalidRange
     *          날짜 범위가 유효하지 않거나, 최대 허용 범위(6개월)를 초과하는 경우
     *
     */
    @Transactional(readOnly = true)
    public List<OccupiedDayResDto> getOccupiedDays(Long buildingId, LocalDate from, LocalDate to) {
        validateRange(from, to);
        var days = dayRepo.findByBuildingIdAndDayBetweenOrderByDayAsc(buildingId, from, to);
        return OccupiedDayResDto.fromEntities(days);
    }

    /**
     * 예약 취소
     *
     * @param reservationId 취소할 예약의 ID
     * @param role 예약 취소 요청자 역할
     *
     * @throws com.ilkan.exception.ReservationExceptions.ReservationNotFound
     *         지정한 ID의 예약이 존재하지 않는 경우
     * @throws com.ilkan.exception.ReservationExceptions.CancelNotAllowed
     *         예약 상태가 IN_USE 또는 COMPLETE인 경우
     * @throws com.ilkan.exception.ReservationExceptions.Forbidden
     *         요청자가 수행자나 건물 소유자가 아닌 경우
     */
    public void cancel(Long reservationId, Role role) {
        Reservation r = reservationRepo.findById(reservationId)
                .orElseThrow(ReservationExceptions.ReservationNotFound::new);

        // 1. 예약 취소 조건
        if (r.getReservationStatus() == ReservationStatus.IN_USE) {
            throw new ReservationExceptions.CancelNotAllowed("체크인 이후에는 취소할 수 없습니다.");
        }
        if (r.getReservationStatus() == ReservationStatus.COMPLETE) {
            throw new ReservationExceptions.CancelNotAllowed("이미 이용이 완료된 예약입니다.");
        }
        if (r.getReservationStatus() == ReservationStatus.CANCELED) {
            return;
        }

        // 2) performer 본인 예약 또는 해당 건물 owner여야 함
        Long mappedRequesterId = switch (role) {
            case PERFORMER -> r.getPerformerId().getId();           // 이 예약의 수행자
            case OWNER     -> r.getBuildingId().getOwner().getId();  // 이 예약 건물의 소유자
            default -> null;
        };

        if (mappedRequesterId == null) {
            throw new ReservationExceptions.Forbidden();
        }

        r.cancel();
        dayRepo.deleteByReservationId(reservationId);
    }
}
