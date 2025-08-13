package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.userdto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserBuildingResDto {
    private Long reservationId;
    private UserResponseDto performer;
    private Long buildingId;
    private String buildingAddress;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAccepted;
    private ReservationStatus buildingStatus;

    public static UserBuildingResDto fromEntity(Reservation reservation) {
        return UserBuildingResDto.builder()
                .reservationId(reservation.getId())
                .performer(UserResponseDto.fromEntity(reservation.getPerformerId()))
                .buildingId(reservation.getBuildingId().getId())
                .buildingAddress(reservation.getBuildingId().getBuildingAddress())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .isAccepted(reservation.getIsAccepted())
                .buildingStatus(reservation.getBuildingStatus())
                .build();
    }
}
