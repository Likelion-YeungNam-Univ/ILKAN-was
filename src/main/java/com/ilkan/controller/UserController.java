package com.ilkan.controller;

import com.ilkan.auth.AllowedRoles;
import com.ilkan.controller.api.UserApi;
import com.ilkan.domain.enums.Role;
import com.ilkan.dto.userdto.UserProfileResDto;
import com.ilkan.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "profile", description = "프로필 조회 API")
public class UserController implements UserApi {
    private final UserService userService;

    @AllowedRoles({Role.PERFORMER, Role.OWNER, Role.REQUESTER})
    @GetMapping("/myprofile")
    public UserProfileResDto getUserProfile (
            @RequestHeader("X-Role") Role role) {
        return userService.getUserProfile(role);
    }
}
