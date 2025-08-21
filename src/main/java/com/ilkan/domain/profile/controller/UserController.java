package com.ilkan.domain.profile.controller;

import com.ilkan.security.AllowedRoles;
import com.ilkan.domain.profile.api.UserApi;
import com.ilkan.domain.profile.entity.enums.Role;
import com.ilkan.domain.profile.dto.UserProfileResDto;
import com.ilkan.domain.profile.service.UserService;
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
