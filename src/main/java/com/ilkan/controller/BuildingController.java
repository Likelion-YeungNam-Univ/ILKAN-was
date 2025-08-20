package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.controller.api.BuildingCommandApi;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.buildingdto.BuildingCreateReq;
import com.ilkan.dto.buildingdto.BuildingCreateRes;
import com.ilkan.service.BuildingService;
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
}

