package com.ilkan.domain.profile.controller;

import com.ilkan.domain.profile.api.UserWorkApi;
import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.dto.performer.WorkStatusReqDto;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.work.dto.performer.AlReadyAppliedResDto;
import com.ilkan.domain.work.dto.performer.WorkApplyReqDto;
import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyDetailResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyListResDto;
import com.ilkan.domain.work.service.WorkService;
import com.ilkan.domain.work.service.WorkSetStatusService;
import com.ilkan.security.AllowedRoles;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/myprofile/commissions")
@RequiredArgsConstructor
@Tag(name = "UserWork", description = "사용자 기반 일거리 API")
public class UserWorkController implements UserWorkApi {
    private final WorkService workService;
    private final WorkSetStatusService workSetStatusService;

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

    // 내가 등록한 진행중 일거리 조회 (의뢰자)
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/working")
    public ResponseEntity<Page<WorkResDto>> doingWorksByRequester(
            @RequestHeader("X-Role") String roleHeader,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WorkResDto> worksDto = workService.doingWorksByRequester(roleHeader, pageable);
        return ResponseEntity.ok(worksDto);
    }

    // 의뢰자 전용 상태 변경 (예: 의뢰자 준비완료 -> status = IN_PROGRESS 요청,
    // 의뢰자 보수지급 관련 요청 등)
    @AllowedRoles(Role.REQUESTER)
    @PatchMapping("{taskId}/status/requester")
    public ResponseEntity<WorkResDto> updateWorkStatusByRequester(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long taskId,
            @RequestBody WorkStatusReqDto request
    ) {
        WorkResDto updated = workSetStatusService.updateWorkStatus(roleHeader, taskId, request);
        return ResponseEntity.ok(updated);
    }

    // 수행자 전용 상태 변경 (예: 수행자 준비완료, 수행완료 눌렀을 때)
    @AllowedRoles(Role.PERFORMER)
    @PatchMapping("{taskId}/status/performer")
    public ResponseEntity<WorkResDto> updateWorkStatusByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long taskId,
            @RequestBody WorkStatusReqDto request
    ) {
        WorkResDto updated = workSetStatusService.updateWorkStatus(roleHeader, taskId, request);
        return ResponseEntity.ok(updated);
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
    public ResponseEntity<AlReadyAppliedResDto> applyWork(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @RequestBody WorkApplyReqDto dto
    ) {
        AlReadyAppliedResDto res = workService.applyWork(role, taskId, dto);
        if (res.isAlreadyApplied()) {
            // 의미적으로 충돌이므로 409 반환 (body에 기존 지원서 포함)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }
        return ResponseEntity.ok(res);
    }


    // 의뢰자 기준 수행자들이 지원한 지원서 목록조회
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/{taskId}")
    public ResponseEntity<Page<WorkApplyListResDto>> getApplicants(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long taskId,
            @PageableDefault(sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WorkApplyListResDto> applicants = workService.getApplicantsByRequester(roleHeader, taskId, pageable);
        return ResponseEntity.ok(applicants);
    }

    // 의뢰자기준 수행자들이 지원한 지원서 상세 조회
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/{taskId}/applies/{applyId}")
    public ResponseEntity<WorkApplyDetailResDto> getWorkApplyDetail(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @PathVariable Long applyId
    ) {
        WorkApplyDetailResDto response = workService.getWorkApplyDetail(role, taskId, applyId);
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
