package com.ilkan.domain.profile.api;

import com.ilkan.security.AllowedRoles;
import com.ilkan.domain.profile.dto.performer.WorkStatusReqDto;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.work.dto.requester.ApplicationResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyDetailResDto;
import com.ilkan.domain.work.dto.performer.WorkApplyReqDto;
import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.work.dto.requester.WorkApplyListResDto;
import com.ilkan.exception.ApiErrorResponse;
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

@Tag(name = "UserWork", description = "사용자 기반 일거리 조회 API")
@RequestMapping(value = "/api/v1/myprofile/commissions", produces = "application/json")
public interface UserWorkApi {

    @Operation(summary = "내가 등록한 일거리 조회", description = "의뢰자(REQUESTER) 역할 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"REQUESTER_FORBIDDEN","message":"의뢰자 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/commissions/upload","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "등록한 일거리가 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"WORK_NOT_FOUND","message":"등록한 일거리가 없습니다.","status":404,
                     "path":"/api/v1/myprofile/commissions/upload","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            )
    })

    @GetMapping("/upload")
    ResponseEntity<Page<WorkResDto>> getMyUploadedWorks(
            @Parameter(description = "요청자 역할 (REQUESTER)", required = true, example = "REQUESTER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") Pageable pageable
    );

    @Operation(
            summary = "일거리 상태 변경",
            description = "의뢰자(REQUESTER) 전용. 준비완료 버튼 클릭 시 IN_PROGRESS, 보수지급 버튼 클릭 시 COMPLETED로 상태 변경"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkResDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                        {"code":"REQUESTER_FORBIDDEN",
                         "message":"의뢰자 권한이 없습니다.",
                         "status":403,
                         "path":"/api/v1/works/{workId}/status",
                         "timestamp":"2025-08-20T15:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "해당 일거리 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                        {"code":"WORK_NOT_FOUND",
                         "message":"해당 일거리가 존재하지 않습니다.",
                         "status":404,
                         "path":"/api/v1/works/{workId}/status",
                         "timestamp":"2025-08-20T15:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "409", description = "상태 전환 불가",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                        {"code":"INVALID_STATUS_TRANSITION",
                         "message":"현재 상태에서 요청한 상태로 전환할 수 없습니다.",
                         "status":409,
                         "path":"/api/v1/works/{workId}/status",
                         "timestamp":"2025-08-20T15:00:00Z"}""")
                    )
            )
    })
    @AllowedRoles(Role.REQUESTER)
    @PatchMapping("{workId}/status")
     ResponseEntity<WorkResDto> updateWorkStatus(
            @RequestHeader("X-Role") String roleHeader,
            @PathVariable Long workId,
            @RequestBody WorkStatusReqDto request
    );

    @Operation(summary = "내가 수행중인 일거리 조회", description = "수행자(PERFORMER) 역할 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"PERFORMER_FORBIDDEN","message":"수행자 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/commissions/doing","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "수행중인 일거리가 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"WORK_NOT_FOUND","message":"수행중인 일거리가 없습니다.","status":404,
                     "path":"/api/v1/myprofile/commissions/doing","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/doing")
    ResponseEntity<Page<WorkResDto>> doingWorksByPerformer(
            @Parameter(description = "요청자 역할 (PERFORMER)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") Pageable pageable
    );

    @Operation(summary = "내가 지원한 일거리 조회", description = "수행자(PERFORMER) 역할 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"PERFORMER_FORBIDDEN","message":"수행자 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/commissions/applied","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "지원한 일거리가 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"WORK_NOT_FOUND","message":"지원한 일거리가 없습니다.","status":404,
                     "path":"/api/v1/myprofile/commissions/applied","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/applied")
    ResponseEntity<Page<ApplicationResDto>> getAppliedWorks(
            @Parameter(description = "요청자 역할 (PERFORMER)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") Pageable pageable
    );

    @Operation(summary = "일거리 지원", description = "수행자(PERFORMER)가 특정 일거리(taskId)에 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (이미 지원했거나 지원 불가능한 상태)"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일거리(taskId)")
    })
    @PostMapping("/{taskId}/requests")
    ResponseEntity<ApplicationResDto> applyWork(
            @RequestHeader("X-Role") String role,
            @PathVariable Long taskId,
            @RequestBody WorkApplyReqDto dto
    );

    @Operation(
            summary = "내 일거리에 지원한 수행자 조회",
            description = "의뢰자(REQUESTER) 역할 전용: 본인이 등록한 일거리에 지원한 수행자들의 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WorkApplyListResDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                        {"code":"REQUESTER_FORBIDDEN","message":"의뢰자 권한이 없습니다.","status":403,
                         "path":"/api/v1/myprofile/commissions/applicants","timestamp":"2025-08-18T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "지원자가 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                        {"code":"APPLICANTS_NOT_FOUND","message":"지원자가 없습니다.","status":404,
                         "path":"/api/v1/myprofile/commissions/applicants","timestamp":"2025-08-18T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/applicants")
    ResponseEntity<Page<WorkApplyListResDto>> getApplicants(
            @Parameter(description = "요청자 역할 (REQUESTER)", required = true, example = "REQUESTER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보")
            Pageable pageable
    );

    @Operation(
            summary = "의뢰자 기준 수행자 지원서 상세 조회",
            description = "의뢰자가 자신이 등록한 일거리에 지원한 특정 수행자의 지원서를 상세 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkApplyDetailResDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"REQUESTER_FORBIDDEN","message":"의뢰자 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/commissions/{workId}/applies/{applyId}","timestamp":"2025-08-19T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "지원서 또는 일거리 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code":"APPLICATION_NOT_FOUND","message":"해당 지원서를 찾을 수 없습니다.","status":404,
                     "path":"/api/v1/myprofile/commissions/{workId}/applies/{applyId}","timestamp":"2025-08-19T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/{workId}/applies/{applyId}")
    ResponseEntity<WorkApplyDetailResDto> getWorkApplyDetail(
            @Parameter(description = "요청자 역할 (REQUESTER)", required = true, example = "REQUESTER")
            @RequestHeader("X-Role") String role,

            @Parameter(description = "조회할 일거리 ID", required = true, example = "1")
            @PathVariable Long workId,

            @Parameter(description = "조회할 지원서 ID", required = true, example = "10")
            @PathVariable Long applyId
    );

    @Operation(
            summary = "의뢰자 기준 수행자 승인",
            description = "의뢰자가 자신이 등록한 일거리(taskId)에 지원한 수행자(performerId)를 승인하고 상태를 IN_PROGRESS로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkApplyListResDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                {"code":"REQUESTER_FORBIDDEN","message":"의뢰자 권한이 없습니다.","status":403,
                                 "path":"/api/v1/tasks/{taskId}/approve/{performerId}","timestamp":"2025-08-19T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "해당 수행자 또는 일거리 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                                {"code":"NOT_FOUND","message":"해당 수행자 또는 일거리를 찾을 수 없습니다.","status":404,
                                 "path":"/api/v1/tasks/{taskId}/approve/{performerId}","timestamp":"2025-08-19T14:00:00Z"}""")
                    )
            )
    })
    @AllowedRoles(Role.REQUESTER)
    @PostMapping("/{taskId}/approve/{performerId}")
    ResponseEntity<WorkApplyListResDto> approvePerformer(
            @Parameter(description = "요청자 역할 (REQUESTER)", required = true, example = "REQUESTER")
            @RequestHeader("X-Role") String roleHeader,

            @Parameter(description = "승인할 일거리 ID", required = true, example = "1")
            @PathVariable Long taskId,

            @Parameter(description = "승인할 수행자 ID", required = true, example = "10")
            @PathVariable Long performerId
    );

}

