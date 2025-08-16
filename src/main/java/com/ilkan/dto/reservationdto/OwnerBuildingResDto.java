package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.BuildingTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OwnerBuildingResDto {

    @Schema(description = "건물 주소", example = "서울시 강남구 테헤란로 123")
    private String buildingAddress;

    @Schema(description = "건물 이미지 URL", example = "https://example.com/images/building.jpg")
    private String buildingImage;

    @Schema(description = "건물 태그", example = "OFFICE_SPACE")
    private BuildingTag buildingTag;

    public static OwnerBuildingResDto fromEntity(Reservation reservation) {
        return OwnerBuildingResDto.builder()
                .buildingAddress(reservation.getBuildingId().getBuildingAddress())
                .buildingImage(reservation.getBuildingId().getBuildingImage())
                .buildingTag(reservation.getBuildingId().getBuildingTag())
                .build();
    }


}
