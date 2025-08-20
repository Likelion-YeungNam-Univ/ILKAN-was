package com.ilkan.controller.api;

import com.ilkan.dto.buildingdto.BuildingCreateReq;
import com.ilkan.dto.buildingdto.BuildingCreateRes;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BuildingCommand", description = "공간(건물) 등록 API")
@RequestMapping(value = "/api/v1/buildings", produces = "application/json")
public interface BuildingCommandApi {

    @Operation(
            summary = "건물주 공간 등록",
            description = """
                    건물주(OWNER) 역할의 유저가 새로운 건물 등록
                    
                    - 요청 헤더: **X-Role = OWNER** 필수
                    - 동일 소유자는 동일한 이름의 건물을 등록할 수 없습니다.
                    - 지역(region), 태그(tag) 값은 Enum 형식(대문자)이어야 합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BuildingCreateRes.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 파라미터 오류 (enum 값, 형식 불일치 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "BUILDING_INVALID_REGION",
                                            value = """
                                                    {
                                                      "code": "BUILDING_INVALID_REGION",
                                                      "message": "유효하지 않은 지역: ABCD",
                                                      "status": 400,
                                                      "path": "/api/v1/buildings",
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "BUILDING_INVALID_TAG",
                                            value = """
                                                    {
                                                      "code": "BUILDING_INVALID_TAG",
                                                      "message": "유효하지 않은 태그: WRONG_TAG",
                                                      "status": 400,
                                                      "path": "/api/v1/buildings",
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "X-Role 헤더 누락/유효하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "ROLE_MISSING",
                                            value = """
                                                    {
                                                      "code": "ROLE_MISSING",
                                                      "message": "헤더 X-Role 이 필요합니다.",
                                                      "status": 401,
                                                      "path": "/api/v1/buildings",
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "ROLE_INVALID",
                                            value = """
                                                    {
                                                      "code": "ROLE_INVALID",
                                                      "message": "유효하지 않은 역할: PERFORMER",
                                                      "status": 401,
                                                      "path": "/api/v1/buildings",
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "해당 역할이 접근할 수 없는 경우",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "ACCESS_DENIED",
                                    value = """
                                            {
                                              "code": "ACCESS_DENIED",
                                              "message": "해당 역할은 해당 서비스에 접근할 수 없습니다.",
                                              "status": 403,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 역할 유저 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "USER_NOT_FOUND",
                                    value = """
                                            {
                                              "code": "USER_NOT_FOUND",
                                              "message": "역할에 해당하는 유저가 없습니다. =OWNER",
                                              "status": 404,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "동일 소유자 중복 건물명",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BUILDING_DUPLICATED_NAME",
                                    value = """
                                            {
                                              "code": "BUILDING_DUPLICATED_NAME",
                                              "message": "동일 소유자의 중복 건물명: 스타트업센터",
                                              "status": 409,
                                              "path": "/api/v1/buildings",
                                              "timestamp": "2025-08-20T10:00:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "DB/데이터 오류",
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
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "BUILDING_DB_DATA_CORRUPTED",
                                            value = """
                                                    {
                                                      "code": "BUILDING_DB_DATA_CORRUPTED",
                                                      "message": "building_region 컬럼에 잘못된 값이 있습니다.",
                                                      "status": 500,
                                                      "path": "/api/v1/buildings",
                                                      "timestamp": "2025-08-20T10:00:00"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping
    ResponseEntity<BuildingCreateRes> createBuilding(
            @Parameter(description = "역할 헤더(OWNER)", required = true, example = "OWNER")
            @RequestHeader("X-Role") String roleHeader,
            @RequestBody BuildingCreateReq request
    );
}