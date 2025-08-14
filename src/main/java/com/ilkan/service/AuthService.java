package com.ilkan.service;

import com.ilkan.exception.RoleExceptions;
import com.ilkan.domain.enums.Role;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    /**
     * 주어진 역할 문자열이 유효한지 검사한 후, 해당하는 {@link Role} 로 변환
     *
     * - null 또는 공백인 경우 {@link RoleExceptions.Missing} 예외
     * - 매칭 실패 시 {@link RoleExceptions.Invalid} 예외
     *
     * @param roleStr 검증할 역할 문자열
     * @return 매칭된 Role
     */
    public Role validateRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) {
            throw new RoleExceptions.Missing();
        }
        final String key = roleStr.trim().toUpperCase();
        try {
            return Role.valueOf(key);
        } catch (IllegalArgumentException e) {
            throw new RoleExceptions.Invalid(roleStr);
        }
    }
}
