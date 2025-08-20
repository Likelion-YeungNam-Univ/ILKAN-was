package com.ilkan.dto.buildingdto;

import com.ilkan.domain.entity.Building;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class BuildingCreateRes {

    @Schema(description = "빌딩 ID", example = "101")
    private final Long id;

    @Schema(description = "빌딩 이름", example = "경산시 분위기 좋은 공유 오피스")
    private final String buildingName;

    @Schema(description = "빌딩 주소", example = "경상북도 경산시 중앙로 123")
    private final String buildingAddress;

    @Schema(description = "빌딩 상태", example = "REGISTERED")
    private final String buildingStatus;

    public static BuildingCreateRes fromEntity(Building b) {
        return BuildingCreateRes.builder()
                .id(b.getId())
                .buildingName(b.getBuildingName())
                .buildingAddress(b.getBuildingAddress())
                .buildingStatus(b.getBuildingStatus() != null ? b.getBuildingStatus().name() : null)
                .build();
    }
}
