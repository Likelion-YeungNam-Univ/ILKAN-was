package com.ilkan.domain.building.entity;

import com.ilkan.domain.profile.entity.User;
import com.ilkan.domain.building.entity.enums.BuildingStatus;
import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id; // 건물 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // 건물주

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "building_address", nullable = false)
    private String buildingAddress; // 건물 주소

    @Column(name = "is_legal", nullable = false)
    private boolean islegal; // 법적 여부

    @Column(name = "building_image", nullable = false)
    private String buildingImage; // 건물 대표 이미지

    @Column(name = "building_image1")
    private String buildingImage1;

    @Column(name = "building_image2")
    private String buildingImage2;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_region", nullable = false)
    private Region buildingRegion; // 건물 지역

    @Enumerated(EnumType.STRING)
    @Column(name = "building_tag", nullable = false)
    private BuildingTag buildingTag; // 건물 태그

    @Column(name = "building_description", columnDefinition = "TEXT", nullable = false)
    private String buildingDescription;

    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime;

    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime;

    @Column(name = "building_name", nullable = false)
    private String buildingName; // 건물 이름

    @Column(name = "building_price", nullable = false)
    private Long buildingPrice;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_status")
    private BuildingStatus buildingStatus; // 건물 심사상태

    // ==== 변경 메서드 ====

    public void updateBuildingImage (String buildingImage) { this.buildingImage = buildingImage; }

    public void updateBuildingImage1 (String buildingImage1) {
        this.buildingImage1 = buildingImage1;
    }

    public void updateBuildingImage2 (String buildingImage2) {
        this.buildingImage2 = buildingImage2;
    }
}