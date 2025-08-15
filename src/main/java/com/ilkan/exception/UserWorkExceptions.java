package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class UserWorkExceptions {
    private UserWorkExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 1. 일거리가 존재하지 않는 경우
    public static class WorkNotFound extends Base {
        public WorkNotFound() { super("일거리가 없습니다."); }
        @Override public String code() { return "WORK_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 2. 수행자 권한 없음
    public static class PerformerForbidden extends Base {
        public PerformerForbidden() { super("수행자 권한이 없습니다."); }
        @Override public String code() { return "PERFORMER_FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }

    // 3. 의뢰자 권한 없음
    public static class RequesterForbidden extends Base {
        public RequesterForbidden() { super("의뢰자 권한이 없습니다."); }
        @Override public String code() { return "REQUESTER_FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }

    // 4. 이미 지원한 일거리인 경우
    public static class AlreadyApplied extends Base {
        public AlreadyApplied() { super("이미 지원한 일거리입니다."); }
        @Override public String code() { return "ALREADY_APPLIED"; }
        @Override public HttpStatus status() { return HttpStatus.CONFLICT; }
    }

    // 5. 잘못된 요청 파라미터
    public static class InvalidRequest extends Base {
        public InvalidRequest(String msg) { super(msg); }
        @Override public String code() { return "INVALID_REQUEST"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    // 6. 의뢰자가 등록한 일거리가 없는 경우
    public static class NoUploadedWorks extends Base {
        public NoUploadedWorks() { super("등록한 일거리가 없습니다."); }
        @Override public String code() { return "NO_UPLOADED_WORKS"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 7. 수행자가 수행중인 일거리가 없는 경우
    public static class NoDoingWorks extends Base {
        public NoDoingWorks() { super("수행중인 일거리가 없습니다."); }
        @Override public String code() { return "NO_DOING_WORKS"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 8. 수행자가 지원한 일거리가 없는 경우
    public static class NoAppliedWorks extends Base {
        public NoAppliedWorks() { super("지원한 일거리가 없습니다."); }
        @Override public String code() { return "NO_APPLIED_WORKS"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }
}

