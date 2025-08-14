package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.userdto.UserRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserBuildingResDto {
    private Long reservationId;
    private UserRespDto performer;
    private Long buildingId;
    private String buildingAddress;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAccepted;
    private ReservationStatus reservationStatus;

    public static UserBuildingResDto fromEntity(Reservation reservation) {
        return UserBuildingResDto.builder()
                .reservationId(reservation.getId())
                .performer(UserRespDto.fromEntity(reservation.getPerformerId()))
                .buildingId(reservation.getBuildingId().getId())
                .buildingAddress(reservation.getBuildingId().getBuildingAddress())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
