package com.ilkan.domain.building.controller;

import com.ilkan.security.AllowedRoles;
import com.ilkan.domain.building.api.BuildingCommandApi;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.building.dto.BuildingCreateReq;
import com.ilkan.domain.building.dto.BuildingCreateRes;
import com.ilkan.domain.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public class BuildingController implements BuildingCommandApi {

    private final BuildingService buildingService;

    @Override
    @AllowedRoles(Role.OWNER)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BuildingCreateRes> createBuilding(
            @RequestHeader("X-Role") String roleHeader,
            @RequestPart("data") @Valid BuildingCreateReq req,         // JSON
            @RequestPart("mainImage") MultipartFile mainImage,         // 파일
            @RequestPart(value="subImage1", required=false) MultipartFile subImage1,
            @RequestPart(value="subImage2", required=false) MultipartFile subImage2
    ) {
        return ResponseEntity.ok(
                buildingService.createBuilding(roleHeader, req, mainImage, subImage1, subImage2)
        );
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

