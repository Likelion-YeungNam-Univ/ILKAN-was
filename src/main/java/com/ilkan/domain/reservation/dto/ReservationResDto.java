package com.ilkan.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilkan.domain.reservation.entity.Reservation;
import com.ilkan.domain.reservation.entity.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResDto {

    private Long reservationId;
    private Long buildingId;
    private Long performerId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;

    public static ReservationResDto fromEntity(Reservation r) {
        return ReservationResDto.builder()
                .reservationId(r.getId())
                .buildingId(r.getBuildingId().getId())
                .performerId(r.getPerformerId().getId())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .reservationStatus(r.getReservationStatus())
                .build();
    }

}
