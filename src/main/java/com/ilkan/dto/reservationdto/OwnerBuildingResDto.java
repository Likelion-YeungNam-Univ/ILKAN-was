package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.enums.BuildingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "건물주가 등록한 건물조회 DTO")
public class OwnerBuildingResDto {

    @Schema(description = "건물 이미지 URL", example = "https://example.com/images/building.jpg")
    private String buildingImage;

    @Schema(description = "건물 이름", example = "경산시 공유오피스")
    private String buildingName;

    @Schema(description = "건물 심사 상태", example = "심사 신청")
    private BuildingStatus buildingStatus;

    @Schema(description = "가격", example = "200원")
    private Long buildingPrice;

    public static OwnerBuildingResDto fromEntity(Building building) {
        return OwnerBuildingResDto.builder()
                .buildingImage(building.getBuildingImage())
                .buildingName(building.getBuildingName())
                .buildingStatus(building.getBuildingStatus())
                .buildingPrice(building.getBuildingPrice())
                .build();
    }


}
