package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class BuildingCommandExceptions {
    private BuildingCommandExceptions() {
    }

    public static abstract class Base extends RuntimeException {
        public Base(String message) {
            super(message);
        }

        public abstract String code();

        public abstract HttpStatus status();
    }

    // 400
    public static class InvalidRegion extends Base {
        private final String region;

        public InvalidRegion(String region) {
            super("잘못된 지역 값입니다: \"" + region + "\"");
            this.region = region;
        }

        @Override
        public String code() {
            return "BUILDING_INVALID_REGION";
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.BAD_REQUEST;
        }

        public String region() {
            return region;
        }
    }

    public static class InvalidTag extends Base {
        private final String tag;

        public InvalidTag(String tag) {
            super("잘못된 태그 값입니다: \"" + tag + "\"");
            this.tag = tag;
        }

        @Override
        public String code() {
            return "BUILDING_INVALID_TAG";
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.BAD_REQUEST;
        }

        public String tag() {
            return tag;
        }
    }

    public static class DuplicatedName extends Base {
        private final String name;

        public DuplicatedName(String name) {
            super("동일한 이름의 건물이 이미 존재합니다: \"" + name + "\"");
            this.name = name;
        }

        @Override
        public String code() {
            return "BUILDING_DUPLICATED_NAME";
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }

        public String name() {
            return name;
        }
    }

    // 404
    public static class NotFound extends Base {
        private final Long id;

        public NotFound(Long id) {
            super("건물을 찾을 수 없습니다: " + id);
            this.id = id;
        }

        @Override
        public String code() {
            return "BUILDING_NOT_FOUND";
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.NOT_FOUND;
        }

        public Long id() {
            return id;
        }
    }

    // 409
    public static class DeleteBlockedByReservations extends Base {
        private final Long buildingId;
        private final Long count; // null 가능: exists만 썼을 때

        public DeleteBlockedByReservations(Long buildingId, Long count) {
            super("해당 건물에 예약이 존재하여 삭제할 수 없습니다.");
            this.buildingId = buildingId;
            this.count = count;
        }

        @Override
        public String code() {
            return "BUILDING_DELETE_BLOCKED_BY_RESERVATIONS";
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }

        public Long buildingId() {
            return buildingId;
        }

        public Long count() {
            return count;
        }
    }
}
