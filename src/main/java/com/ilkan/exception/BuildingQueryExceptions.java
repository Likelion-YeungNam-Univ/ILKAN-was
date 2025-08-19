package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class BuildingQueryExceptions {
    private BuildingQueryExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 400 계열
    public static class InvalidPage extends Base {
        public InvalidPage() { super("page는 0 이상이어야 합니다."); }
        @Override public String code() { return "BUILDING_INVALID_PAGE"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    public static class InvalidSize extends Base {
        public InvalidSize() { super("size는 1이상, 50이하여야 합니다."); }
        @Override public String code() { return "BUILDING_INVALID_SIZE"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    // 500 계열
    public static class DbDataCorrupted extends Base {
        public DbDataCorrupted(String msg) { super(msg); }
        @Override public String code() { return "BUILDING_DB_DATA_CORRUPTED"; }
        @Override public HttpStatus status() { return HttpStatus.INTERNAL_SERVER_ERROR; }
    }

    public static class DbError extends Base {
        public DbError(String msg) { super(msg); }
        @Override public String code() { return "BUILDING_DB_ERROR"; }
        @Override public HttpStatus status() { return HttpStatus.INTERNAL_SERVER_ERROR; }
    }
}
