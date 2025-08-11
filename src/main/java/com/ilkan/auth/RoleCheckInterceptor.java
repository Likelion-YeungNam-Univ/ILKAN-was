package com.ilkan.auth;

import com.ilkan.domain.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

// role check 여부 판단 && 받아온 role이 허용 목록에 있는지 확인
@Component
public class RoleCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (!(handler instanceof HandlerMethod method)) return true;

        AllowedRoles allowed = method.getMethodAnnotation(AllowedRoles.class);
        if (allowed == null) allowed = method.getBeanType().getAnnotation(AllowedRoles.class);
        if (allowed == null) return true; // 제한 없음

        String roleStr = req.getHeader("X-Role");
        if (roleStr == null || roleStr.isBlank()) throw new RoleExceptions.Missing();

        final Role reqRole;
        try { reqRole = Role.valueOf(roleStr.toUpperCase()); }
        catch (IllegalArgumentException e) { throw new RoleExceptions.Invalid(roleStr); }

        boolean permitted = Arrays.asList(allowed.value()).contains(reqRole);
        if (!permitted) throw new RoleExceptions.Denied();

        return true;
    }
}
