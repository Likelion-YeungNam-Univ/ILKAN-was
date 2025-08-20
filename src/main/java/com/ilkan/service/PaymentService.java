package com.ilkan.service;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.reservationdto.ReservationPayReqDto;
import com.ilkan.dto.reservationdto.ReservationPayResDto;
import com.ilkan.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationPayResDto processFakePayment(Long reservationId, ReservationPayReqDto req) {
        // 1) 예약 조회 (id 기반)
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found: " + reservationId));

        // 시연용 카드정보 간단 검증
        if (req != null) {
            // 카드번호가 제공되면 숫자와 대시(-)만 허용하는 간단한 포맷 체크
            if (req.getCardNumber() != null && !req.getCardNumber().matches("[0-9-]+")) {
                throw new IllegalArgumentException("cardNumber 형식이 올바르지 않습니다.");
            }
            // 만약 만료일(expiry) 포맷 체크를 원하면 간단히 검증 가능 (MM/YY)
            if (req.getExpiry() != null && !req.getExpiry().matches("(0[1-9]|1[0-2])/(\\d{2})")) {
                throw new IllegalArgumentException("expiry 형식은 MM/YY 이어야 합니다.");
            }
        }

        // 2) 시간 유효성 검사
        LocalDateTime start = reservation.getStartTime();
        LocalDateTime end = reservation.getEndTime();
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("퇴실 시간은 입실 시간보다 이후여야 합니다.");
        }

        // 3) 대여일수 계산: inclusive 방식 (예: 13 -> 21 = 9일)
        long daysBetween = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
        long rentalDaysPrimitive = daysBetween + 1;
        if (rentalDaysPrimitive <= 0) rentalDaysPrimitive = 1L;
        // 불필요한 명시적 박싱(Long.valueOf) 제거 — 필요하면 reservation.updateRentalInfo에서 primitive로 받게 바꾸는 게 더 깔끔
        Long rentalDays = rentalDaysPrimitive; // 자동 박싱

        // 4) 하루 단가: reservation에서 building 참조로 가져오기
        Building building = reservation.getBuildingId();
        if (building == null) throw new IllegalStateException("Reservation has no building associated.");

        Long dailyPrice = building.getBuildingPrice();
        if (dailyPrice == null) throw new IllegalStateException("Building price not configured.");

        // 5) 총액 계산
        Long totalPrice = rentalDays * dailyPrice;

        // 6) 예약 업데이트 (rentalDays, totalPrice, reservation status)
        reservation.updateRentalInfo(rentalDays, totalPrice);
        reservation.updateReservationStatus(ReservationStatus.PAY_USEWAITING); // 네 도메인 상태로 설정

        reservationRepository.save(reservation);

        // 8) 응답 반환
        return ReservationPayResDto.fromEntity(reservation);
    }
}
