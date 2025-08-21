package com.ilkan.domain.building.api;

import com.ilkan.domain.building.entity.enums.BuildingTag;
import com.ilkan.domain.building.entity.enums.Region;
import com.ilkan.domain.building.dto.BuildingCardResDto;
import com.ilkan.domain.building.dto.BuildingDetailResDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BuildingQuery", description = "공간(건물) 조회 API")
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public interface BuildingQueryApi {

    @Operation(
            summary = "공간 목록 조회",
            description = """
            공간 카드 리스트를 페이지네이션하여 반환합니다.
            - 정렬: 최신순(id desc) 고정
            - page는 0부터 시작
            - size 기본값: 15 (허용: 1~50)
            - region/tag는 선택값(없으면 전체)
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingCardPage.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "파라미터 오류(page/size 범위, enum 형식 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "BUILDING_INVALID_PAGE",
                                            value = """
                                            {
                                              "code": "BUILDING_INVALID_PAGE",
                                              "message": "page는 0 이상이어야 합니다.",
                                              "status": 400,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-19T10:00:00"
                                            }"""
                                    ),
                                    @ExampleObject(
                                            name = "BUILDING_INVALID_SIZE",
                                            value = """
                                            {
                                              "code": "BUILDING_INVALID_SIZE",
                                              "message": "size는 1이상, 50이하여야 합니다.",
                                              "status": 400,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-19T10:00:00"
                                            }"""
                                    ),
                                    @ExampleObject(
                                            name = "INVALID_ENUM",
                                            value = """
                                            {
                                              "code": "INVALID_ENUM",
                                              "message": "파라미터 'region' 값 'hihi'은(는) 허용되지 않습니다. 허용값: [SEOUL, BUSAN, GYEONGGI, GYEONGBUK, INCHEON, DAEGU]",
                                              "status": 400,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-19T10:00:00"
                                            }"""
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "DB 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "BUILDING_DB_ERROR",
                                            value = """
                                            {
                                              "code": "BUILDING_DB_ERROR",
                                              "message": "database error",
                                              "status": 500,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-19T10:00:00"
                                            }"""
                                    ),
                                    @ExampleObject(
                                            name = "BUILDING_DB_DATA_CORRUPTED",
                                            value = """
                                            {
                                              "code": "BUILDING_DB_DATA_CORRUPTED",
                                              "message": "building_region 컬럼에 잘못된 값이 있습니다.",
                                              "status": 500,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-19T10:00:00"
                                            }"""
                                    )
                            }
                    )
            )
    })
    @GetMapping
    Page<BuildingCardResDto> list(
            @ParameterObject
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,

            @Parameter(description = "지역(시/도) 필터", required = false,
                    schema = @Schema(implementation = Region.class), example = "SEOUL")
            @RequestParam(required = false) Region region,

            @Parameter(description = "태그 필터", required = false,
                    schema = @Schema(implementation = BuildingTag.class), example = "OFFICE_SPACE")
            @RequestParam(required = false) BuildingTag tag
    );


    @Operation(
            summary = "공간 상세 조회",
            description = """
            단일 공간(건물)의 상세 정보를 반환합니다.
            - PathVariable id는 1 이상 정수여야 합니다.
            - 가격 단위는 DAY 고정(KRW).
            - checkIn/checkOut은 HH:mm 형식의 문자열입니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDetailResDto.class))),

            @ApiResponse(responseCode = "400", description = "잘못된 요청(id 범위 또는 타입)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "BUILDING_INVALID_ID",
                                            value = """
                                            {
                                              "code": "BUILDING_INVALID_ID",
                                              "message": "id는 1 이상이어야 합니다. id=0",
                                              "status": 400,
                                              "path": "/api/v1/buildings/0",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }"""
                                    ),
                                    @ExampleObject(
                                            name = "ARG_TYPE_MISMATCH",
                                            value = """
                                            {
                                              "code": "ARG_TYPE_MISMATCH",
                                              "message": "파라미터 'id' 값 'abc' 형식이 올바르지 않습니다.",
                                              "status": 400,
                                              "path": "/api/v1/buildings/abc",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }"""
                                    )
                            })),

            @ApiResponse(responseCode = "404", description = "대상 건물을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BUILDING_NOT_FOUND",
                                    value = """
                                    {
                                      "code": "BUILDING_NOT_FOUND",
                                      "message": "해당 건물을 찾을 수 없습니다. id=99999",
                                      "status": 404,
                                      "path": "/api/v1/buildings/99999",
                                      "timestamp": "2025-08-20T10:00:00"
                                    }"""
                            ))),

            @ApiResponse(responseCode = "500", description = "DB/데이터 무결성 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "BUILDING_DB_ERROR",
                                            value = """
                                            {
                                              "code": "BUILDING_DB_ERROR",
                                              "message": "database error",
                                              "status": 500,
                                              "path": "/api/v1/buildings/55",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }"""
                                    ),
                                    @ExampleObject(
                                            name = "BUILDING_DB_DATA_CORRUPTED",
                                            value = """
                                            {
                                              "code": "BUILDING_DB_DATA_CORRUPTED",
                                              "message": "building_tag 컬럼에 잘못된 값이 있습니다.",
                                              "status": 500,
                                              "path": "/api/v1/buildings/55",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }"""
                                    )
                            }))
    })
    @GetMapping("/{id}")
    BuildingDetailResDto detail(
            @Parameter(description = "건물 ID(1 이상 정수)", example = "55")
            @PathVariable Long id
    );
}
