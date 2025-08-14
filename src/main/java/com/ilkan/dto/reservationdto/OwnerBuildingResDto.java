package com.ilkan.dto.reservationdto;

import com.ilkan.domain.entity.Reservation;
import com.ilkan.domain.enums.BuildingTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OwnerBuildingResDto {
    private String buildingAddress; // 건물 주소
    private String buildingImage;   // 건물 이미지
    private BuildingTag buildingTag; // 건물 태그

    public static OwnerBuildingResDto fromEntity(Reservation reservation) {
        return OwnerBuildingResDto.builder()
                .buildingAddress(reservation.getBuildingId().getBuildingAddress())
                .buildingImage(reservation.getBuildingId().getBuildingImage())
                .buildingTag(reservation.getBuildingId().getBuildingTag())
                .build();
    }


}
