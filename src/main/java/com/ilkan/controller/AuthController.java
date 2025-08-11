package com.ilkan.controller;

import com.ilkan.dto.userdto.LoginRequestDto;
import com.ilkan.dto.userdto.LoginResponseDto;
import com.ilkan.domain.enums.Role;
import com.ilkan.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    /* 역할 버튼 로그인: 모든 요청에 X-Role 헤더로 보내면 인터셉터가 검증 */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestHeader("X-Role") String roleHeader) {
        Role role = authService.validateRole(roleHeader);
        return ResponseEntity.ok(new LoginResponseDto(role.name(), "로그인 성공"));
    }

}
