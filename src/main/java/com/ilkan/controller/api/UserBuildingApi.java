package com.ilkan.controller.api;

import com.ilkan.dto.reservationdto.OwnerBuildingResDto;
import com.ilkan.dto.reservationdto.OwnersInUseResDto;
import com.ilkan.dto.reservationdto.UserBuildingResDto;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "UserBuilding", description = "사용자/건물주 기반 건물 조회 API")
@RequestMapping(value = "/api/v1/myprofile/buildings", produces = "application/json")
public interface UserBuildingApi {

    @Operation(summary = "사용중인 건물 조회", description = "수행자(PERFORMER) 역할 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserBuildingResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code": "PERFORMER_FORBIDDEN","message":"수행자 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/buildings/using","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용중인 건물이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code": "USER_BUILDING_NOT_FOUND","message":"사용중인 건물이 없습니다.","status":404,
                     "path":"/api/v1/myprofile/buildings/using","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/using")
    ResponseEntity<Page<UserBuildingResDto>> getUsingBuildings(
            @Parameter(description = "수행자 역할 (PERFORMER)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") @PageableDefault Pageable pageable
    );

    @Operation(summary = "등록한 건물 조회", description = "건물주(OWNER) 역할 전용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OwnerBuildingResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code": "OWNER_FORBIDDEN","message":"건물주 권한이 없습니다.","status":403,
                     "path":"/api/v1/myprofile/buildings/registered","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "등록한 건물이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {"code": "OWNER_BUILDING_NOT_FOUND","message":"등록한 건물이 없습니다.","status":404,
                     "path":"/api/v1/myprofile/buildings/registered","timestamp":"2025-08-15T14:00:00Z"}""")
                    )
            )
    })
    @GetMapping("/registered")
    ResponseEntity<Page<OwnerBuildingResDto>> getRegisteredBuildings(
            @Parameter(description = "건물주 역할 (OWNER)", required = true, example = "OWNER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") @PageableDefault Pageable pageable
    );

    @Operation(summary = "사용중인 건물 조회", description = "건물주(OWNER) 역할 전용, 수행자가 사용중인 자신의 건물 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OwnersInUseResDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"OWNER_FORBIDDEN\",\"message\":\"건물주 권한이 없습니다.\",\"status\":403,\"path\":\"/api/v1/myprofile/buildings/in-use\",\"timestamp\":\"2025-08-15T14:00:00Z\"}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용중인 건물이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"OWNER_BUILDING_NOT_FOUND\",\"message\":\"사용중인 건물이 없습니다.\",\"status\":404,\"path\":\"/api/v1/myprofile/buildings/in-use\",\"timestamp\":\"2025-08-15T14:00:00Z\"}")
                    )
            )
    })
    @GetMapping("/inuse")
    ResponseEntity<Page<OwnersInUseResDto>> getBuildingsInUse(
            @Parameter(description = "건물주 역할 (OWNER)", required = true, example = "OWNER")
            @RequestHeader("X-Role") String roleHeader,
            @Parameter(description = "페이지네이션 정보") @PageableDefault Pageable pageable

    );


}
