package com.ilkan.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GeminiExceptions {

    @Getter
    public static class BadRequest extends RuntimeException {
        private final HttpStatus status = HttpStatus.BAD_REQUEST;
        public BadRequest(String msg) { super(msg); }
    }

    @Getter
    public static class UpstreamError extends RuntimeException {
        private final HttpStatus status = HttpStatus.BAD_GATEWAY;
        public UpstreamError(String msg) { super(msg); }
    }

    @Getter
    public static class ParseError extends RuntimeException {
        private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        public ParseError(String msg) { super(msg); }
    }
}
