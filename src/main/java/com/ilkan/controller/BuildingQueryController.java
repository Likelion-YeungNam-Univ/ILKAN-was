package com.ilkan.controller;

import com.ilkan.controller.api.BuildingQueryApi;
import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardResDto;
import com.ilkan.exception.BuildingQueryExceptions;
import com.ilkan.service.BuildingQueryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public class BuildingQueryController implements BuildingQueryApi {

    private final BuildingQueryService service;

    private static final int DEFAULT_SIZE = 15;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 50;

    @Override
    @GetMapping
    public Page<BuildingCardResDto> list(
            @ParameterObject
            @PageableDefault(size = DEFAULT_SIZE, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) BuildingTag tag
    ) {

        if (pageable.getPageNumber() < 0) {
            throw new BuildingQueryExceptions.InvalidPage();
        }
        int size = pageable.getPageSize();
        if (size < MIN_SIZE || size > MAX_SIZE) {
            throw new BuildingQueryExceptions.InvalidSize();
        }

        Pageable fixed = PageRequest.of(pageable.getPageNumber(), size, Sort.by(Sort.Direction.DESC, "id"));

        return service.search(region, tag, fixed);
    }
}
