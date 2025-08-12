package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.BuildingStatus;
import com.ilkan.dto.userdto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResDto {
    private Long reservationId;
    private UserResponseDto performer;
    private Long buildingId;
    private String buildingAddress;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAccepted;
    private BuildingStatus buildingStatus;

    public static ReservationResDto fromEntity(Reservation reservation) {
        return ReservationResDto.builder()
                .reservationId(reservation.getId())
                .performer(UserResponseDto.fromEntity(reservation.getPerformer()))
                .buildingId(reservation.getBuilding().getId())
                .buildingAddress(reservation.getBuilding().getBuildingAddress())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .isAccepted(reservation.getIsAccepted())
                .buildingStatus(reservation.getBuildingStatus())
                .build();
    }
}
