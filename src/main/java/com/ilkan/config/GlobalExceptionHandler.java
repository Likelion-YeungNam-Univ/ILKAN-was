package com.ilkan.config;

import com.ilkan.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 : Role 계열
    @ExceptionHandler(RoleExceptions.Base.class)
    public ResponseEntity<?> handleRole(RoleExceptions.Base e, HttpServletRequest req) {
        return ResponseEntity.status(e.status()).body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    // 커스텀 : Reservation 계열
    @ExceptionHandler(ReservationExceptions.Base.class)
    public ResponseEntity<ApiErrorResponse> handleReservation(ReservationExceptions.Base e,
                                                              HttpServletRequest req) {
        return ResponseEntity.status(e.status()).body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    // 커스텀 : Building 조회 계열
    @ExceptionHandler(BuildingQueryExceptions.Base.class)
    public ResponseEntity<ApiErrorResponse> handleBuildingQuery(BuildingQueryExceptions.Base e,
                                                                HttpServletRequest req) {
        return ResponseEntity.status(e.status())
                .body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    // 커스텀 : Building Command 계열
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

    // PathVariable/RequestParam 타입 캐스팅 실패
    @ExceptionHandler(BuildingCommandExceptions.Base.class)
    public ResponseEntity<ApiErrorResponse> handleBuildingCommand(BuildingCommandExceptions.Base e, HttpServletRequest req) {
        return ResponseEntity.status(e.status()).body(apiError(e.code(), e.getMessage(), e.status().value(), req.getRequestURI()));
    }

    // @Valid 바디 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBody(MethodArgumentNotValidException e, HttpServletRequest req) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(apiError("REQUEST_BODY_INVALID", details, 400, req.getRequestURI()));
    }

    // Bean Validation 직접 던지는 경우
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException e, HttpServletRequest req) {
        String details = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(apiError("CONSTRAINT_VIOLATION", details, 400, req.getRequestURI()));
    }

    // JOSN 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(apiError("BODY_NOT_READABLE", "요청 본문을 파싱할 수 없습니다.", 400, req.getRequestURI()));
    }

    // 데이터 무결성 위반 (UNIQUE, FK, CHECK 등)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        // 상황별로 409(중복) 또는 400(무결성 위반) 선택 가능
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(apiError("DATA_INTEGRITY_VIOLATION", "데이터 무결성 제약을 위반했습니다.", 409, req.getRequestURI()));
    }

    // DB 접근 오류
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccess(DataAccessException e,
                                                             HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError("BUILDING_DB_ERROR", "database error", 500, req.getRequestURI()));
    }

    // DB enum값 깨짐
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException e,
                                                                  HttpServletRequest req) {
        String msg = "enum 매핑 중 잘못된 값이 발견되었습니다.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError("BUILDING_DB_DATA_CORRUPTED", msg, 500, req.getRequestURI()));
    }

    // 메서드 미지원
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(apiError("METHOD_NOT_ALLOWED", "지원하지 않는 메서드입니다.", 405, req.getRequestURI()));
    }

    // 최종 Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError("INTERNAL_ERROR", "서버 에러가 발생했습니다.", 500, req.getRequestURI()));
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
