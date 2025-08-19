package com.ilkan.dto.buildingdto;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BuildingCardRespDto {

    @Schema(description = "빌딩 ID", example = "101")
    private final Long id;

    @Schema(description = "건물주명", example = "아년석")
    private final String owner;

    @Schema(description = "대표 이미지 URL")
    private final String buildingImage;      // building_image

    @Schema(description = "가격(원)", example = "50000")
    private final Long price;               // building_price

    @Schema(description = "지역(시/도)", example = "GYEONGBUK")
    private final Region region;            // building_region

    @Schema(description = "태그", example = "SHARED_OFFICE")
    private final BuildingTag tag;          // building_tag

    // Entity -> DTO 변환
    public static BuildingCardRespDto fromEntity(Building b) {
        return BuildingCardRespDto.builder()
                .id(b.getId())
                .owner(b.getOwner() != null ? b.getOwner().getName() : null)
                .buildingImage(b.getBuildingImage())
                .price(b.getBuildingPrice())
                .region(b.getBuildingRegion())
                .tag(b.getBuildingTag())
                .build();
    }
}
