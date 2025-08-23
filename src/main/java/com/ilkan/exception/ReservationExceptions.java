// com.ilkan.reservation.ReservationExceptions.java
package com.ilkan.exception;

import org.springframework.http.HttpStatus;

public final class ReservationExceptions {
    private ReservationExceptions() {}

    public static abstract class Base extends RuntimeException {
        public Base(String message) { super(message); }
        public abstract String code();
        public abstract HttpStatus status();
    }

    // 1. 빌딩을 찾을 수 없는 경우
    public static class BuildingNotFound extends Base {
        public BuildingNotFound() { super("해당 건물을 찾을 수 없습니다."); }
        @Override public String code() { return "BUILDING_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 2. 수행자를 찾을 수 없는 경우
    public static class PerformerNotFound extends Base {
        public PerformerNotFound() { super("해당 수행자를 찾을 수 없습니다."); }
        @Override public String code() { return "PERFORMER_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 3. 시작일과 종료일이 같거나, 6개월 이상인 경우
    public static class InvalidRange extends Base {
        public InvalidRange(String msg) { super(msg); }
        @Override public String code() { return "INVALID_RANGE"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    // 4. 이미 점유된 날짜인 경우
    public static class AlreadyOccupied extends Base {
        public AlreadyOccupied() { super("해당 기간에 이미 예약이 있습니다."); }
        @Override public String code() { return "RESERVATION_ALREADY_OCCUPIED"; }
        @Override public HttpStatus status() { return HttpStatus.CONFLICT; }
    }

    // 5. 예약이 존재하지 않을 경우
    public static class ReservationNotFound extends Base {
        public ReservationNotFound() { super("예약을 찾을 수 없습니다."); }
        @Override public String code() { return "RESERVATION_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    // 6. 체크인 이후 취소하거나, 이미 완료된 예약일 경우
    public static class CancelNotAllowed extends Base {
        public CancelNotAllowed(String msg) { super(msg); }
        @Override public String code() { return "RESERVATION_CANCEL_NOT_ALLOWED"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    // 7. 권한이 없을 경우
    public static class Forbidden extends Base {
        public Forbidden() { super("권한이 없습니다."); }
        @Override public String code() { return "FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }
}
