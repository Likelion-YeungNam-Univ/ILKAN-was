package com.ilkan.dto.userdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "역할 로그인 응답 DTO")
public class LoginResponseDto {

    @Schema(description = "사용자 역할", example = "CLIENT")
    private final String role;

    @Schema(description = "로그인 결과 메시지", example = "로그인 성공")
    private final String message;

}
