package com.ilkan.exception;

import com.ilkan.domain.enums.Role;
import org.springframework.http.HttpStatus;

public final class RoleExceptions {
    private RoleExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 1. X-ROLE 헤더가 없는 경우
    public static class Missing extends Base {
        public Missing() { super("헤더 X-Role 이 필요합니다."); }
        @Override public String code() { return "ROLE_MISSING"; }
        @Override public HttpStatus status() { return HttpStatus.UNAUTHORIZED; }
    }

    // 2. X-ROLE 값이 유효하지 않은 경우
    public static class Invalid extends Base {
        public Invalid(String role) { super("유효하지 않은 역할: " + role); }
        @Override public String code() { return "ROLE_INVALID"; }
        @Override public HttpStatus status() { return HttpStatus.UNAUTHORIZED; }
    }

    // 3. 해당 역할이 접근 권한 없는 서비스에 접근할 경우
    public static class Denied extends Base {
        public Denied() { super("해당 역할은 해당 서비스에 접근할 수 없습니다."); }
        @Override public String code() { return "ACCESS_DENIED"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }

    // 4. 주어진 역할에 해당하는 유저가 없을 때
    public static class NotFound extends Base {
        private final Role role;
        public NotFound(Role role) {
            super("역할에 해당하는 유저가 없습니다. =" + role);
            this.role = role;
        }
        @Override public String code() { return "USER_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
        public Role role() { return role; }
    }
}
