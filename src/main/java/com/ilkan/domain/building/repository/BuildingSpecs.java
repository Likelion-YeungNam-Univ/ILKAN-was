package com.ilkan.domain.building.repository;

import com.ilkan.domain.building.entity.Building;
import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import org.springframework.data.jpa.domain.Specification;

public final class BuildingSpecs {
    private BuildingSpecs() {}

    // building_region
    public static Specification<Building> regionEq(Region region) {
        return (region == null) ? null
                : (root, q, cb) -> cb.equal(root.get("buildingRegion"), region);
    }

    // building_tag
    public static Specification<Building> tagEq(BuildingTag tag) {
        return (tag == null) ? null
                : (root, q, cb) -> cb.equal(root.get("buildingTag"), tag);
    }
}
