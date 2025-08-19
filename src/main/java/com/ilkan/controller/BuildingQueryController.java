package com.ilkan.controller;

import com.ilkan.controller.api.BuildingQueryApi;
import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardRespDto;
import com.ilkan.exception.BuildingQueryExceptions;
import com.ilkan.service.BuildingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<BuildingCardRespDto> list(
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) BuildingTag tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        if (page < 0) throw new BuildingQueryExceptions.InvalidPage();

        int s = (size == null) ? DEFAULT_SIZE : size;
        if (s < MIN_SIZE || s > MAX_SIZE) throw new BuildingQueryExceptions.InvalidSize();

        Pageable pageable = PageRequest.of(page, s, Sort.by(Sort.Direction.DESC, "id"));
        return service.search(region, tag, pageable);
    }
}
