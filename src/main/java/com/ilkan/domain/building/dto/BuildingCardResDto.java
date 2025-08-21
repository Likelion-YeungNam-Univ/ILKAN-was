package com.ilkan.domain.building.dto;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BuildingCardResDto {

    @Schema(description = "빌딩 ID", example = "101")
    private final Long id;

    @Schema(description = "건물주명", example = "아년석")
    private final String owner;

    @Schema(description = "대표 이미지 URL")
    private final String buildingImage;      // building_image

    @Schema(description = "가격(원)", example = "50000")
    private final Long buildingPrice;        // building_price

    @Schema(description = "지역(시/도)", example = "GYEONGBUK")
    private final Region region;            // building_region

    @Schema(description = "태그", example = "OFFICE_SPACE")
    private final BuildingTag tag;          // building_tag

    @Schema(description = "빌딩 이름", example = "경산시 분위기 좋은 공유 오피스")
    private final String buildingName;

    public static BuildingCardResDto fromEntity(Building b) {
        return BuildingCardResDto.builder()
                .id(b.getId())
                .owner(b.getOwner().getName())
                .buildingImage(b.getBuildingImage())
                .buildingPrice(b.getBuildingPrice())
                .region(b.getBuildingRegion())
                .tag(b.getBuildingTag())
                .buildingName(b.getBuildingName())
                .build();
    }
}
