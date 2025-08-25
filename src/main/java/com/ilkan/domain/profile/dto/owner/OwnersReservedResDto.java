package com.ilkan.domain.profile.dto.owner;

import com.ilkan.domain.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = " 건물주가 올린 건물 중 예약된 건물조회 DTO")
public class OwnersReservedResDto {

    @Schema(description = "예약 ID", example = "1")
    private final Long reservationId;

    @Schema(description = "빌딩 ID", example = "1")
    private final Long buildingId;

    @Schema(description = "건물 이름", example = "경산시 공유오피스")
    private final String buildingName;

    @Schema(description = "건물주 이름", example = "이얏호")
    private final String ownerName;

    @Schema(description = "건물 이미지 URL", example = "https://example.com/images/building.jpg")
    private final String buildingImage;

    @Schema(description = "건물 예약 시작 시간", example = "2025-07-21T09:00:00")
    private final LocalDateTime startTime;

    @Schema(description = "건물 예약 종료 시간", example = "2025-09-21T18:00:00")
    private final LocalDateTime endTime;

    public static OwnersReservedResDto fromEntity(Reservation reservation) {
        return OwnersReservedResDto.builder()
                .reservationId(reservation.getId())                        // 예약 ID
                .buildingId(reservation.getBuildingId().getId())           // 건물 ID
                .buildingName(reservation.getBuildingId().getBuildingName()) // 건물 이름
                .ownerName(reservation.getBuildingId().getOwner().getName()) // 건물주 이름
                .buildingImage(reservation.getBuildingId().getBuildingImage())     // 건물 이미지
                .startTime(reservation.getStartTime())                     // 예약 시작 시간
                .endTime(reservation.getEndTime())                         // 예약 종료 시간
                .build();
    }
}
