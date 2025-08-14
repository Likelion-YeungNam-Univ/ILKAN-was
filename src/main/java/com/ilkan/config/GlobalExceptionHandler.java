package com.ilkan.config;

import com.ilkan.exception.ApiErrorResponse;
import com.ilkan.exception.ReservationExceptions;
import com.ilkan.exception.RoleExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleExceptions.Base.class)
    public ResponseEntity<?> handleRole(RoleExceptions.Base e, HttpServletRequest req) {
        return ResponseEntity.status(e.status()).body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    @ExceptionHandler(ReservationExceptions.Base.class)
    public ResponseEntity<ApiErrorResponse> handleReservation(ReservationExceptions.Base e,
                                                              HttpServletRequest req) {
        return ResponseEntity.status(e.status()).body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    private ApiErrorResponse apiError(String code, String message, int status, String path) {
        return ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now()) // ApiErrorResponse가 LocalDateTime을 사용하므로 맞춰줌
                .build();
    }

}
