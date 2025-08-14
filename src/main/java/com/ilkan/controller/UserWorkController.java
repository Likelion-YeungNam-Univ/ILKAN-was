package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.workdto.WorkResDto;
import com.ilkan.service.WorkService;
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
@RequestMapping("/api/v1/myprofile/commissions")
@RequiredArgsConstructor
public class UserWorkController {
    private final WorkService workService;

    // 내가 등록한 일거리 조회 (의뢰자)
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/upload")
    public ResponseEntity<Page<WorkResDto>> getMyUploadedWorks(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 서비스 호출하여 DTO로 변환된 페이징 결과 조회
        Page<WorkResDto> worksDto = workService.getWorksByRequester(roleHeader, pageable);
        if (worksDto.isEmpty()) {
            return ResponseEntity.ok().body(Page.empty()); // 성공 및 데이터는 없음
        }
        return ResponseEntity.ok(worksDto);
    }

    // 내가 수행중인 일거리 조회 (수행자)
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/doing")
    public ResponseEntity<Page<WorkResDto>> doingWorksByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WorkResDto> worksDto = workService.doingWorksByPerformer(roleHeader, pageable);
        if (worksDto.isEmpty()) {
            return ResponseEntity.ok().body(Page.empty()); // 성공 및 데이터는 없음
        }
        return ResponseEntity.ok(worksDto);
    }

    // 내가 지원한 일거리 조회 (수행자)
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/applied")
    public ResponseEntity<Page<WorkResDto>> getAppliedWorks(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WorkResDto> appliedWorks = workService.getAppliedWorksByPerformer(roleHeader, pageable);
        if (appliedWorks.isEmpty()) {
            return ResponseEntity.ok().body(Page.empty()); // 성공 및 데이터는 없음
        }
        return ResponseEntity.ok(appliedWorks);
    }

}
