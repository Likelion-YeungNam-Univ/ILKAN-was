package com.ilkan.domain.work.api;

import com.ilkan.domain.work.entity.enums.WorkCategory;
import com.ilkan.domain.work.dto.WorkDetailResDto;
import com.ilkan.domain.work.dto.WorkListResDto;
import com.ilkan.domain.work.dto.requester.WorkReqDto;
import com.ilkan.domain.profile.dto.performer.WorkResDto;
import com.ilkan.domain.profile.dto.requester.WorkUserResDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Works", description = "일거리 조회 및 관리 API")
@RequestMapping(value = "/api/v1/works", produces = "application/json")
public interface WorksApi {

    @Operation(summary = "카테고리별 일거리 조회", description = "선택한 카테고리에 해당하는 일거리를 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    ResponseEntity<Page<WorkListResDto>> getWorkList(
            @Parameter(description = "조회할 일거리 카테고리 (필수)")
            @RequestParam WorkCategory category, // 필수 파라미터로 변경

            @Parameter(description = "페이지네이션 정보 (기본 createdAt DESC 정렬)")
            @PageableDefault(sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    );


    @Operation(summary = "일거리 상세 조회", description = "taskId로 특정 일거리의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkDetailResDto.class))),
            @ApiResponse(responseCode = "404", description = "일거리를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{taskId}")
    ResponseEntity<WorkDetailResDto> getWorkDetail(
            @Parameter(description = "조회할 일거리 ID", example = "1") @PathVariable Long taskId
    );

    @Operation(summary = "일거리 등록", description = "의뢰자가 새로운 일거리를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    ResponseEntity<WorkResDto> createWork(
            @Parameter(description = "사용자 역할 헤더", example = "REQUESTER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "등록할 일거리 정보", required = true)
            @RequestBody WorkReqDto dto
    );

    @Operation(summary = "일거리 수정", description = "의뢰자가 자신의 일거리를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkUserResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "일거리를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{taskId}")
    ResponseEntity<WorkUserResDto> updateWork(
            @Parameter(description = "수정할 일거리 ID", example = "1") @PathVariable Long taskId,
            @Parameter(description = "사용자 역할 헤더", example = "REQUESTER") @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "수정할 일거리 정보", required = true) @RequestBody WorkReqDto dto
    );

    @Operation(summary = "일거리 삭제", description = "의뢰자가 자신의 일거리를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "일거리를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{taskId}")
    ResponseEntity<Void> deleteWork(
            @Parameter(description = "삭제할 일거리 ID", example = "1") @PathVariable Long taskId,
            @Parameter(description = "사용자 역할 헤더", example = "REQUESTER") @RequestHeader("X-Role") String roleHeader
    );
}
