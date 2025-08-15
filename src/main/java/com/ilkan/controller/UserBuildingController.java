package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.controller.api.UserBuildingApi;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.reservationdto.OwnerBuildingResDto;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.service.UserBuildingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/myprofile/buildings")
@RequiredArgsConstructor
@Tag(name = "UserBuilding", description = "사용자/건물주 기반 건물 조회 API")
public class UserBuildingController implements UserBuildingApi {

    private final UserBuildingService userBuildingService;
    private final UserBuildingService ownerBuildingService;

    // 수행자 - 사용중인 건물조회
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/using")
    public ResponseEntity<Page<UserBuildingResDto>> getUsingBuildings(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserBuildingResDto> reservations = userBuildingService.findUsingBuildingsByPerformer(roleHeader, pageable);
        return ResponseEntity.ok(reservations);
    }

    // 건물주 - 자신이 등록한 건물 조회
    @AllowedRoles(Role.OWNER)
    @GetMapping("/registered")
    public ResponseEntity<Page<OwnerBuildingResDto>> getRegisteredBuildings(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OwnerBuildingResDto> buildings = ownerBuildingService.getRegisteredBuildings(roleHeader, pageable);
        return ResponseEntity.ok(buildings);
    }


}
