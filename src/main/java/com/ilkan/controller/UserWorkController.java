package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.controller.api.UserWorkApi;
import com.ilkan.domain.entity.TaskApplication;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.workdto.*;
import com.ilkan.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/myprofile/commissions")
@RequiredArgsConstructor
@Tag(name = "UserWork", description = "사용자 기반 일거리 API")
public class UserWorkController implements UserWorkApi {
    private final WorkService workService;

    // 내가 등록한 일거리 조회 (의뢰자)
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/upload")
    public ResponseEntity<Page<WorkResDto>> getMyUploadedWorks(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 서비스 호출하여 DTO로 변환된 페이징 결과 조회
        Page<WorkResDto> worksDto = workService.getWorksByRequester(roleHeader, pageable);
        return ResponseEntity.ok(worksDto);
    }

    // 진행중인 일거리 조회 (의뢰자 프로필 상단)
    @AllowedRoles(Role.REQUESTER) // 의뢰자만 가능
    @PatchMapping("{workId}/status")
    public ResponseEntity<WorkResDto> updateWorkStatus(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    ) {
        // 요청 바디에서 status 꺼내서 서비스에 전달
        WorkResDto updated = workService.updateWorkStatus(roleHeader, workId, request.getStatus());
        return ResponseEntity.ok(updated); // 응답은 WorkResDto (응답 DTO)
    }


    // 내가 수행중인 일거리 조회 (수행자)
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/doing")
    public ResponseEntity<Page<WorkResDto>> doingWorksByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<WorkResDto> worksDto = workService.doingWorksByPerformer(roleHeader, pageable);
        return ResponseEntity.ok(worksDto);
    }

    // 내가 지원한 일거리 조회 (수행자)
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/applied")
    public ResponseEntity<Page<ApplicationResDto>> getAppliedWorks(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ApplicationResDto> appliedWorks = workService.getAppliedWorksByPerformer(roleHeader, pageable);
        return ResponseEntity.ok(appliedWorks);
    }

    // 일거리 지원 (수행자)
    @AllowedRoles(Role.PERFORMER)
    @PostMapping("/{taskId}/requests")
    public ResponseEntity<ApplicationResDto> applyWork(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @RequestBody WorkApplyReqDto dto
    ) {
        TaskApplication application = workService.applyWork(role, taskId, dto);
        ApplicationResDto response = ApplicationResDto.fromEntity(application);
        return ResponseEntity.ok(response);
    }

    // 의뢰자 기준 수행자들이 지원한 지원서 목록조회
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/applies")
    public ResponseEntity<Page<WorkApplyListResDto>> getApplicants(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WorkApplyListResDto> applicants = workService.getApplicantsByRequester(roleHeader, pageable);
        return ResponseEntity.ok(applicants);
    }

    // 의뢰자기준 수행자들이 지원한 지원서 상세 조회
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/{workId}/applies/{applyId}")
    public ResponseEntity<WorkApplyDetailResDto> getWorkApplyDetail(
            @RequestHeader("X-Role") String role,
            @PathVariable Long workId,
            @PathVariable Long applyId
    ) {
        WorkApplyDetailResDto response = workService.getWorkApplyDetail(role, workId, applyId);
        return ResponseEntity.ok(response);
    }

    // 의뢰자기준 수행자 선택
    @AllowedRoles(Role.REQUESTER)
    @PostMapping("/{taskId}/approve/{performerId}")
    public ResponseEntity<WorkApplyListResDto> approvePerformer(
            @RequestHeader("X-Role") String roleHeader, // 요청자 권한
            @PathVariable Long taskId,
            @PathVariable Long performerId) {

        WorkApplyListResDto approvedPerformer = workService.approvePerformer(roleHeader, taskId, performerId);
        return ResponseEntity.ok(approvedPerformer);
    }

}
