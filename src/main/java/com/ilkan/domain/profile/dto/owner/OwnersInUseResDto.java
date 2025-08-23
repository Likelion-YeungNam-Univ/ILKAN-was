package com.ilkan.domain.profile.dto.owner;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "자신의 건물중 사용중인것을 조회 DTO")
public class OwnersInUseResDto {

    @Schema(description = "예약 ID", example = "1")
    private Long reservationId;

    @Schema(description = "빌딩 ID", example = "1")
    private Long buildingId;

    @Schema(description = "건물 이름", example = "경산시 공유오피스")
    private final String buildingName;

    @Schema(description = "건물 주소", example = "서울시 강남구 테헤란로 123")
    private final String buildingAddress;

    @Schema(description = "건물 이미지 URL", example = "https://example.com/images/building.jpg")
    private final String buildingImage;

    @Schema(description = "체크인 시간", example = "08:00~")
    private final LocalDateTime checkInTime;

    @Schema(description = "체크아웃 시간", example = "~21:00")
    private final  LocalDateTime checkOutTime;

    @Schema(description = "건물 예약시작 시간", example = "07/21")
    private final LocalDateTime startTime;

    @Schema(description = "건물 예약종료 시간", example = "09/21")
    private final LocalDateTime endTime;

    public static OwnersInUseResDto fromEntity(Reservation reservation) {
        Building building = reservation.getBuildingId();
        return OwnersInUseResDto.builder()
                .reservationId(reservation.getId())
                .buildingId(building.getId())
                .buildingName(building.getBuildingName())
                .buildingAddress(building.getBuildingAddress())
                .buildingImage(building.getBuildingImage())
                .checkInTime(reservation.getStartTime())    // 수행자가 실제 체크인한 시간
                .checkOutTime(reservation.getEndTime())    // 수행자가 체크아웃한 시간
                .startTime(reservation.getStartTime())     // 예약 시작 시간
                .endTime(reservation.getEndTime())         // 예약 종료 시간
                .build();
    }
}
