package com.ilkan.controller.api;

import com.ilkan.dto.userdto.LoginResDto;
import com.ilkan.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "역할 기반 로그인 API")
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
public interface AuthApi {

    @Operation(
            summary = "역할 로그인",
            description = "헤더 <code>X-Role</code> 값(PERFORMER/OWNER 등)을 검증하고 토큰 없이 역할만 확인하는 로그인"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "헤더 누락 또는 유효하지 않은 역할 값",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name="ROLE_MISSING",
                                            value="""
                        {"code":"ROLE_MISSING","message":"헤더 X-Role 이 필요합니다.","status":401,
                         "path":"/api/v1/auth/login","timestamp":"2025-08-14T10:00:00Z"}"""),
                                    @ExampleObject(name="ROLE_INVALID",
                                            value="""
                        {"code":"ROLE_INVALID","message":"유효하지 않은 역할: OWNERX","status":401,
                         "path":"/api/v1/auth/login","timestamp":"2025-08-14T10:00:00Z"}""")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "접근 권한이 없는 역할",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name="ACCESS_DENIED",
                                    value="""
                    {"code":"ACCESS_DENIED","message":"해당 역할은 접근할 수 없습니다.","status":403,
                     "path":"/api/v1/auth/login","timestamp":"2025-08-14T10:00:00Z"}""")
                    )
            )
    })
    @PostMapping("/login")
    ResponseEntity<LoginResDto> login(
            @Parameter(description = "요청자 역할(PERFORMER/OWNER 등)", required = true, example = "PERFORMER")
            @RequestHeader("X-Role") String roleHeader
    );
}
