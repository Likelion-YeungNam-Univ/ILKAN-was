package com.ilkan.dto.userdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "역할 로그인 요청 DTO")
public class LoginReqDto {

    @Schema(description = "사용자 역할", example = "REQUESTER")
    private final String role;

}
