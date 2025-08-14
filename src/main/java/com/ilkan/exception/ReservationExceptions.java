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

    public static class BuildingNotFound extends Base {
        public BuildingNotFound() { super("building not found"); }
        @Override public String code() { return "BUILDING_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    public static class PerformerNotFound extends Base {
        public PerformerNotFound() { super("performer not found"); }
        @Override public String code() { return "PERFORMER_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    public static class InvalidRange extends Base {
        public InvalidRange(String msg) { super(msg); }
        @Override public String code() { return "INVALID_RANGE"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    public static class AlreadyOccupied extends Base {
        public AlreadyOccupied() { super("해당 기간에 이미 예약이 있습니다."); }
        @Override public String code() { return "RESERVATION_ALREADY_OCCUPIED"; }
        @Override public HttpStatus status() { return HttpStatus.CONFLICT; }
    }

    public static class ReservationNotFound extends Base {
        public ReservationNotFound() { super("reservation not found"); }
        @Override public String code() { return "RESERVATION_NOT_FOUND"; }
        @Override public HttpStatus status() { return HttpStatus.NOT_FOUND; }
    }

    public static class CancelNotAllowed extends Base {
        public CancelNotAllowed(String msg) { super(msg); }
        @Override public String code() { return "RESERVATION_CANCEL_NOT_ALLOWED"; }
        @Override public HttpStatus status() { return HttpStatus.BAD_REQUEST; }
    }

    public static class Forbidden extends Base {
        public Forbidden() { super("취소 권한이 없습니다."); }
        @Override public String code() { return "FORBIDDEN"; }
        @Override public HttpStatus status() { return HttpStatus.FORBIDDEN; }
    }
}
