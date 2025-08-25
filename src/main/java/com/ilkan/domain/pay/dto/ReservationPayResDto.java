package com.ilkan.domain.pay.dto;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReservationPayResDto {
    private Long reservationId;
    private Long buildingId;
    private String buildingName;
    private String buildingAddress;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long rentalDays;
    private Long totalPrice;

    public static ReservationPayResDto fromEntity(Reservation r) {
        Building b = r.getBuildingId();
        return ReservationPayResDto.builder()
                .reservationId(r.getId())
                .buildingId(b != null ? b.getId() : null)
                .buildingName(b != null ? b.getBuildingName() : null)
                .buildingAddress(b != null ? b.getBuildingAddress() : null)
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .rentalDays(r.getRentalDays())
                .totalPrice(r.getTotalPrice())
                .build();
    }
}
