package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class BuildingCommandExceptions {
    private BuildingCommandExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 400
    public static class InvalidRegion extends Base {
        private final String region;
        public InvalidRegion(String region) { super("유효하지 않은 지역: " + region); this.region = region; }
        @Override public String code() { return "BUILDING_INVALID_REGION"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
        public String region() { return region; }
    }

    public static class InvalidTag extends Base {
        private final String tag;
        public InvalidTag(String tag) { super("유효하지 않은 태그: " + tag); this.tag = tag; }
        @Override public String code() { return "BUILDING_INVALID_TAG"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
        public String tag() { return tag; }
    }

    public static class DuplicatedName extends Base {
        private final String name;
        public DuplicatedName(String name) { super("동일 소유자의 중복 건물명: " + name); this.name = name; }
        @Override public String code() { return "BUILDING_DUPLICATED_NAME"; }
        @Override public HttpStatus status() { return HttpStatus.CONFLICT; }
        public String name() { return name; }
    }
}
