package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class UserBuildingExceptions {
    private UserBuildingExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 1. 수행자가 사용중인 건물이 없는 경우
    public static class UserBuildingNotFound extends Base {
        public UserBuildingNotFound() { super("사용중인 건물이 없습니다."); }
        @Override public String code() { return "USER_BUILDING_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 2. 건물주가 등록한 건물이 없는 경우
    public static class OwnerBuildingNotFound extends Base {
        public OwnerBuildingNotFound() { super("등록한 건물이 없습니다."); }
        @Override public String code() { return "OWNER_BUILDING_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 3. 수행자 권한 없음
    public static class PerformerForbidden extends Base {
        public PerformerForbidden() { super("수행자 권한이 없습니다."); }
        @Override public String code() { return "PERFORMER_FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }

    // 4. 건물주 권한 없음
    public static class OwnerForbidden extends Base {
        public OwnerForbidden() { super("건물주 권한이 없습니다."); }
        @Override public String code() { return "OWNER_FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }

    // 5. 잘못된 요청
    public static class InvalidRequest extends Base {
        public InvalidRequest(String msg) { super(msg); }
        @Override public String code() { return "INVALID_REQUEST"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }
}
