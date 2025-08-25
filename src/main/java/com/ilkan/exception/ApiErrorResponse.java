package com.ilkan.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@Schema(name = "ApiError", description = "표준 에러 응답")
public class ApiErrorResponse {
    @Schema(example = "ROLE_INVALID") String code;
    @Schema(example = "유효하지 않은 역할: OWNERX") String message;
    @Schema(example = "401") int status;
    @Schema(example = "/api/v1/auth/login") String path;
    @Schema(example = "2025-08-14T19:32:10.128") LocalDateTime timestamp;
}
