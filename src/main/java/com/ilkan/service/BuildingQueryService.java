package com.ilkan.service;

import com.ilkan.domain.entity.Building;
import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardRespDto;
import com.ilkan.repository.BuildingRepository;
import com.ilkan.repository.BuildingSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingQueryService {

    private final BuildingRepository repo;

    public Page<BuildingCardRespDto> search(Region region,
                                            BuildingTag tag,
                                            Pageable pageable) {

        Specification<Building> spec = Specification
                .where(BuildingSpecs.regionEq(region))
                .and(BuildingSpecs.tagEq(tag));

        return repo.findAll(spec, pageable)
                .map(BuildingCardRespDto::fromEntity);
    }
}
