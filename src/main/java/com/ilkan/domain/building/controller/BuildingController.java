package com.ilkan.domain.building.controller;

import com.ilkan.security.AllowedRoles;
import com.ilkan.domain.building.api.BuildingCommandApi;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.building.dto.BuildingCreateReq;
import com.ilkan.domain.building.dto.BuildingCreateRes;
import com.ilkan.domain.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public class BuildingController implements BuildingCommandApi {

    private final BuildingService buildingService;

    @Override
    @AllowedRoles(Role.OWNER)
    @PostMapping
    public ResponseEntity<BuildingCreateRes> createBuilding(
            @RequestHeader("X-Role") String roleHeader,
            @Valid @RequestBody BuildingCreateReq req
    ) {
        BuildingCreateRes res = buildingService.createBuilding(roleHeader, req);
        return ResponseEntity
                .created(URI.create("/api/v1/buildings/" + res.getId()))
                .body(res);
    }

    @AllowedRoles(Role.OWNER)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable("id") Long id
    ) {
        buildingService.deleteBuilding(roleHeader, id);
        return ResponseEntity.noContent().build(); // 204
    }
}

