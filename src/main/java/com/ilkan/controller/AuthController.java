package com.ilkan.controller;

import com.ilkan.dto.userdto.LoginResDto;
import com.ilkan.domain.enums.Role;
import com.ilkan.exception.ApiErrorResponse;
import com.ilkan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ilkan.controller.api.AuthApi;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "로그인 API")
public class AuthController implements AuthApi {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    /* 역할 버튼 로그인: 모든 요청에 X-Role 헤더로 보내면 인터셉터가 검증 */
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(
            @RequestHeader("X-Role") String roleHeader) {
        Role role = authService.validateRole(roleHeader);
        String name = authService.getNameByRole(role);
        return ResponseEntity.ok(new LoginResDto(role.name(), "로그인 성공", name));
    }

}
