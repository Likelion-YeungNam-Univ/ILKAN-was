package com.ilkan.domain.reservation.dto;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.reservation.entity.Reservation;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.reservation.entity.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationReqDto {

    private Long buildingId;
    private LocalDate checkInDate;   // 포함
    private LocalDate checkOutDate;  // 제외

    // 체크인 시간 : 15시, 체크아웃 시간 : 11시
    public Reservation toEntity(User performer, Building building) {
        LocalDateTime start = checkInDate.atTime(15, 0);
        LocalDateTime end   = checkOutDate.atTime(11, 0);

        return Reservation.builder()
                .buildingId(building)
                .performerId(performer)
                .startTime(start)
                .endTime(end)
                .reservationStatus(ReservationStatus.RESERVED) // 생성 시 자동 확정(체크인 전 상태)
                .build();
    }
}
