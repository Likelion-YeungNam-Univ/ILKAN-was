package com.ilkan.domain.profile.api;

import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.dto.performer.WorkStatusReqDto;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.work.dto.performer.WorkApplyReqDto;
import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyDetailResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyListResDto;
import com.ilkan.security.AllowedRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자(의뢰자 / 수행자) 기반 마이페이지 API 스펙
 */
@Tag(name = "UserWork", description = "사용자 기반 일거리 조회/행동 API")
@RequestMapping(value = "/api/v1/myprofile/commissions", produces = "application/json")
public interface UserWorkApi {

    /** 내가 등록한 일거리 전체 조회 (의뢰자) */
    @Operation(summary = "내가 등록한 일거리 조회", description = "의뢰자 전용. 전체 일거리 조회(빈 페이지 가능)")
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/upload")
    ResponseEntity<Page<WorkResDto>> getMyUploadedWorks(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    /** 내가 등록한 진행중 일거리 조회 (의뢰자) */
    @Operation(summary = "진행중 일거리 조회", description = "의뢰자 전용. 진행중(IN_PROGRESS 등) 일거리만 조회")
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/working")
    ResponseEntity<Page<WorkResDto>> doingWorksByRequester(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    /** 의뢰자 전용 상태 변경 (준비완료, 보수지급 등) */
    @Operation(summary = "의뢰자 상태 변경", description = "준비완료(IN_PROGRESS), 보수지급(COMPLETE_WAITING/COMPLETED) 등 상태 변경")
    @AllowedRoles(Role.REQUESTER)
    @PatchMapping("{workId}/status/requester")
    ResponseEntity<WorkResDto> updateWorkStatusByRequester(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    );

    /** 수행자 전용 상태 변경 (준비완료, 수행완료 등) */
    @Operation(summary = "수행자 상태 변경", description = "준비완료(IN_PROGRESS), 수행완료(PAY_WAITING/COMPLETED) 등 상태 변경")
    @AllowedRoles(Role.PERFORMER)
    @PatchMapping("{workId}/status/performer")
    ResponseEntity<WorkResDto> updateWorkStatusByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    );

    /** 수행중 일거리 조회 (수행자) */
    @Operation(summary = "수행중 일거리 조회", description = "수행자 전용. ASSIGNED, IN_PROGRESS, PAY_WAITING, COMPLETE_WAITING 상태 조회")
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/doing")
    ResponseEntity<Page<WorkResDto>> doingWorksByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    /** 지원한 일거리 조회 (수행자) */
    @Operation(summary = "지원한 일거리 조회", description = "수행자 전용. 지원한 일거리 목록")
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/applied")
    ResponseEntity<Page<ApplicationResDto>> getAppliedWorks(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    /** 일거리 지원 (수행자) */
    @Operation(summary = "일거리 지원", description = "수행자가 특정 일거리에 지원")
    @AllowedRoles(Role.PERFORMER)
    @PostMapping("/{taskId}/requests")
    ResponseEntity<ApplicationResDto> applyWork(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @RequestBody WorkApplyReqDto dto
    );

    /** 지원자 목록 조회 (의뢰자) */
    @Operation(summary = "지원자 목록 조회", description = "의뢰자가 등록한 일거리에 지원한 수행자 목록 조회")
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/applies")
    ResponseEntity<Page<WorkApplyListResDto>> getApplicants(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    /** 지원서 상세 조회 (의뢰자) */
    @Operation(summary = "지원서 상세 조회", description = "의뢰자 전용")
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/{workId}/applies/{applyId}")
    ResponseEntity<WorkApplyDetailResDto> getWorkApplyDetail(
            @RequestHeader("X-Role") String role,
            @PathVariable Long workId,
            @PathVariable Long applyId
    );

    /** 수행자 승인 (의뢰자) */
    @Operation(summary = "수행자 승인(배정)", description = "의뢰자가 특정 지원자를 승인하여 배정")
    @AllowedRoles(Role.REQUESTER)
    @PostMapping("/{taskId}/approve/{performerId}")
    ResponseEntity<WorkApplyListResDto> approvePerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long taskId,
            @PathVariable Long performerId
    );
}
