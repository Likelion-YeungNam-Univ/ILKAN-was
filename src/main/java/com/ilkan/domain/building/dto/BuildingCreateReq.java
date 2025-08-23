package com.ilkan.domain.building.dto;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.building.entity.enums.BuildingStatus;
import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingCreateReq {

    @Schema(description = "연락처", example = "010-1234-5678")
    @NotBlank
    private String phoneNumber;

    @Schema(description = "건물 주소", example = "경상북도 경산시 중앙로 123")
    @NotBlank
    private String buildingAddress;

    @Schema(description = "빌딩 이름", example = "경산시 분위기 좋은 공유 오피스")
    @NotBlank
    private String buildingName;

    @Schema(description = "가격(원)", example = "50000")
    @NotNull
    @PositiveOrZero
    private Long buildingPrice;

    @Schema(description = "이메일", example = "owner@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "지역 ENUM", example = "SEOUL")
    @NotBlank
    private String buildingRegion;

    @Schema(description = "태그 ENUM", example = "OFFICE_SPACE")
    @NotBlank
    private String buildingTag;

    @Schema(description = "건물 설명", example = "넓고 쾌적한 오피스 공간입니다.")
    @NotBlank
    private String buildingDescription;

    public Building toEntity(User owner, Region region, BuildingTag tag) {
        return Building.builder()
                .owner(owner)
                .phoneNumber(phoneNumber)
                .buildingAddress(buildingAddress)
                .buildingRegion(region)
                .buildingTag(tag)
                .buildingDescription(buildingDescription)
                .checkInTime(LocalTime.of(15, 0))
                .checkOutTime(LocalTime.of(11, 0))
                .buildingName(buildingName)
                .buildingPrice(buildingPrice)
                .email(email)
                .buildingStatus(BuildingStatus.REGISTERED)
                .islegal(true)
                .build();
    }
}
