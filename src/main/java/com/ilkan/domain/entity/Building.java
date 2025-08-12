package com.ilkan.domain.entity;

import com.ilkan.domain.enums.BuildingTag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name = "owner_id", nullable = false)
    private User owner; // 건물주

    @Column(name = "building_address", nullable = false)
    private String buildingAddress; // 건물 주소

    @Column(name = "is_legal", nullable = false)
    private boolean islegal; // 법적 여부

    @Column(name = "building_image", nullable = false)
    private String buildingImage; // 건물 이미지

    @Column(name = "building_region", nullable = false)
    private String buildingRegion; // 건물 지역

    @Enumerated(EnumType.STRING)
    @Column(name = "building_tag", nullable = false)
    private BuildingTag buildingTag; // 건물 태그

    // ==== 변경 메서드 ====
    public void updateOwner (User owner) {
        this.owner = owner;
    }

    public void updateBuildingAddress (String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public void updateIslegal (boolean islegal) { this.islegal = islegal; }

    public void updateBuildingImage (String buildingImage) { this.buildingImage = buildingImage; }

    public void updateBuildingRegion (String buildingRegion) { this.buildingRegion = buildingRegion; }
    // 건물 태그수정 불가, 구현X
}