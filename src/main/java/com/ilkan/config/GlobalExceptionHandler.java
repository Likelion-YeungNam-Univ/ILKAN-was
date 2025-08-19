package com.ilkan.config;

import com.ilkan.exception.ApiErrorResponse;
import com.ilkan.exception.BuildingQueryExceptions;
import com.ilkan.exception.ReservationExceptions;
import com.ilkan.exception.RoleExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(BuildingQueryExceptions.Base.class)
    public ResponseEntity<ApiErrorResponse> handleBuildingQuery(BuildingQueryExceptions.Base e,
                                                                HttpServletRequest req) {
        return ResponseEntity.status(e.status())
                .body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e,
                                                               HttpServletRequest req) {
        String param = e.getName();
        String value = String.valueOf(e.getValue());

        String hint = "";
        Class<?> required = e.getRequiredType();
        if (required != null && required.isEnum()) {
            Object[] allowed = required.getEnumConstants();
            hint = " 허용값: " + java.util.Arrays.toString(allowed);
        }

        return ResponseEntity.badRequest().body(
                apiError("ARG_TYPE_MISMATCH",
                        "파라미터 '" + param + "' 값 '" + value + "' 형식이 올바르지 않습니다." + hint,
                        400, req.getRequestURI())
        );
    }

    private ApiErrorResponse apiError(String code, String message, int status, String path) {
        return ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }



}
