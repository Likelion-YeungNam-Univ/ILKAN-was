package com.ilkan.controller.api;

import com.ilkan.dto.workdto.WorkDetailResDto;
import com.ilkan.dto.workdto.WorkListResDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Works", description = "일거리 조회 API")
@RequestMapping(value = "/api/v1/works", produces = "application/json")
public interface WorksApi {

    @Operation(summary = "모든 일거리 조회", description = "페이지네이션으로 모든 일거리를 조회합니당.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
    })
    @GetMapping
    ResponseEntity<Page<WorkListResDto>> getWorkList(
            @Parameter(description = "페이지네이션 정보 (기본 createdAt DESC 정렬)", required = false)
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
}
