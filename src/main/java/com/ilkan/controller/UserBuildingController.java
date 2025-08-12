package com.ilkan.controller;

import com.ilkan.dto.reservationdto.UserBuildingResDto;
import com.ilkan.service.UserBuildingService;
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
@RequestMapping("/api/v1/my/buildings")
@RequiredArgsConstructor
public class UserBuildingController {

    private final UserBuildingService userBuildingService;

    @GetMapping("/using")
    public ResponseEntity<Page<UserBuildingResDto>> getUsingBuildings(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserBuildingResDto> reservations = userBuildingService.findUsingBuildingsByPerformer(roleHeader, pageable);
        if (reservations.isEmpty()) {
            return ResponseEntity.ok().body(Page.empty());
        }
        return ResponseEntity.ok(reservations);
    }
}
