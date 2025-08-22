package com.ilkan.domain.profile.api;

import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.dto.performer.WorkStatusReqDto;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.work.dto.performer.WorkApplyReqDto;
import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyDetailResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyListResDto;
import com.ilkan.exception.ApiErrorResponse;
import com.ilkan.security.AllowedRoles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserWork", description = "사용자 기반 일거리 조회/행동 API")
@RequestMapping(value = "/api/v1/myprofile/commissions", produces = "application/json")
public interface UserWorkApi {

    @Operation(summary = "내가 등록한 일거리 조회", description = "의뢰자 전용. 전체 일거리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"FORBIDDEN\",\"message\":\"권한이 없습니다.\",\"status\":403}")))
    })
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/upload")
    ResponseEntity<Page<WorkResDto>> getMyUploadedWorks(
            @Parameter(description = "사용자 역할", example = "REQUESTER") @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    @Operation(summary = "진행중 일거리 조회", description = "의뢰자 전용. 진행중(IN_PROGRESS) 일거리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkResDto.class)))
    })
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/working")
    ResponseEntity<Page<WorkResDto>> doingWorksByRequester(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    @Operation(summary = "의뢰자 상태 변경", description = "준비완료 → IN_PROGRESS, 보수지급 → COMPLETED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"INVALID_STATUS\",\"message\":\"잘못된 상태 변경 요청\",\"status\":400}")))
    })
    @AllowedRoles(Role.REQUESTER)
    @PatchMapping("{workId}/status/requester")
    ResponseEntity<WorkResDto> updateWorkStatusByRequester(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    );

    @Operation(summary = "수행자 상태 변경", description = "준비완료 → IN_PROGRESS, 수행완료 → COMPLETED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @AllowedRoles(Role.PERFORMER)
    @PatchMapping("{workId}/status/performer")
    ResponseEntity<WorkResDto> updateWorkStatusByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    );

    @Operation(summary = "수행중 일거리 조회", description = "수행자 전용. ASSIGNED, IN_PROGRESS 상태 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkResDto.class)))
    })
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/doing")
    ResponseEntity<Page<WorkResDto>> doingWorksByPerformer(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    @Operation(summary = "지원한 일거리 조회", description = "수행자 전용. 지원 목록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApplicationResDto.class)))
    })
    @AllowedRoles(Role.PERFORMER)
    @GetMapping("/applied")
    ResponseEntity<Page<ApplicationResDto>> getAppliedWorks(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    @Operation(summary = "일거리 지원", description = "수행자가 특정 일거리에 지원")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원 성공",
                    content = @Content(schema = @Schema(implementation = ApplicationResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @AllowedRoles(Role.PERFORMER)
    @PostMapping("/{taskId}/requests")
    ResponseEntity<ApplicationResDto> applyWork(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @RequestBody WorkApplyReqDto dto
    );

    @Operation(summary = "지원자 목록 조회", description = "의뢰자가 등록한 일거리에 지원한 수행자 목록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkApplyListResDto.class)))
    })
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/applies")
    ResponseEntity<Page<WorkApplyListResDto>> getApplicants(
            @RequestHeader("X-Role") String roleHeader,
            Pageable pageable
    );

    @Operation(summary = "지원서 상세 조회", description = "의뢰자 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WorkApplyDetailResDto.class))),
            @ApiResponse(responseCode = "404", description = "지원서 없음",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @AllowedRoles(Role.REQUESTER)
    @GetMapping("/{workId}/applies/{applyId}")
    ResponseEntity<WorkApplyDetailResDto> getWorkApplyDetail(
            @RequestHeader("X-Role") String role,
            @PathVariable Long workId,
            @PathVariable Long applyId
    );

    @Operation(summary = "수행자 승인(배정)", description = "의뢰자가 특정 지원자를 승인하여 배정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(schema = @Schema(implementation = WorkApplyListResDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @AllowedRoles(Role.REQUESTER)
    @PostMapping("/{taskId}/approve/{performerId}")
    ResponseEntity<WorkApplyListResDto> approvePerformer(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long taskId,
            @PathVariable Long performerId
    );
}
