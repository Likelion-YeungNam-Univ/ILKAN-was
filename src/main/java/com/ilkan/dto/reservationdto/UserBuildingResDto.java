package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.ReservationStatus;
import com.ilkan.dto.userdto.UserRespDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserBuildingResDto {
    @Schema(description = "예약 ID", example = "101")
    private final Long reservationId;

    @Schema(description = "수행자 정보")
    private final UserRespDto performer;

    @Schema(description = "건물 ID", example = "55")
    private final Long buildingId;

    @Schema(description = "건물 주소", example = "서울시 강남구 123")
    private final String buildingAddress;

    @Schema(description = "예약 시작 시간", example = "2025-08-20T09:00:00")
    private final LocalDateTime startTime;

    @Schema(description = "예약 종료 시간", example = "2025-08-20T18:00:00")
    private final LocalDateTime endTime;

    @Schema(description = "예약 상태", example = "REGISTERED")
    private final ReservationStatus reservationStatus;

    @Schema(description = "건물 이름", example = "야호빌딩")
    private final String buildingName;

    public static UserBuildingResDto fromEntity(Reservation reservation) {
        return UserBuildingResDto.builder()
                .reservationId(reservation.getId())
                .performer(UserRespDto.fromEntity(reservation.getPerformerId()))
                .buildingId(reservation.getBuildingId().getId())
                .buildingAddress(reservation.getBuildingId().getBuildingAddress())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .buildingName(reservation.getBuildingId().getBuildingName())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
