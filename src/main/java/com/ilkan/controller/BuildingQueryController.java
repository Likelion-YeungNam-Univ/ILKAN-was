package com.ilkan.controller;

import com.ilkan.domain.enums.BuildingTag;
import com.ilkan.domain.enums.Region;
import com.ilkan.dto.buildingdto.BuildingCardRespDto;
import com.ilkan.service.BuildingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public class BuildingQueryController {

    private final BuildingQueryService service;

    private static final int DEFAULT_SIZE = 15;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");

    @GetMapping
    public ResponseEntity<Page<BuildingCardRespDto>> list(
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) BuildingTag tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        int pageSize = normalizeSize(size);
        Pageable pageable = PageRequest.of(Math.max(0, page), pageSize, parseSort(sort));
        return ResponseEntity.ok(service.search(region, tag, pageable));
    }

    private int normalizeSize(Integer size) {
        int s = (size == null) ? DEFAULT_SIZE : Math.max(3, size);
        int r = s % 3;
        return (r == 0) ? s : (s - r);
    }

    private Sort parseSort(String sort) {
        try {
            String[] parts = sort.split(",");
            String prop = parts[0].trim();
            Sort.Direction dir = (parts.length > 1)
                    ? Sort.Direction.fromString(parts[1].trim())
                    : Sort.Direction.DESC;
            return Sort.by(dir, prop);
        } catch (Exception e) {
            return DEFAULT_SORT;
        }
    }
}
