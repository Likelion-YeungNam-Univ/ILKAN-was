package com.ilkan.controller.api;

import com.ilkan.dto.workdto.WorkResDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkResDto.class))),
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
    ResponseEntity<Page<WorkResDto>> getAppliedWorks(
            @Parameter(description = "요청자 역할 (PERFORMER)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") Pageable pageable
    );
}
