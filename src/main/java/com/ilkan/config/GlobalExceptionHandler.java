package com.ilkan.config;

import com.ilkan.exception.ReservationExceptions;
import com.ilkan.exception.RoleExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleExceptions.Base.class)
    public ResponseEntity<?> handleRole(RoleExceptions.Base e) {
        return ResponseEntity.status(e.status()).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", e.code(),
                "message", e.getMessage()
        ));
    }

    @ExceptionHandler(ReservationExceptions.Base.class)
    public ResponseEntity<Map<String,Object>> handleReservation(ReservationExceptions.Base e,
                                                                HttpServletRequest req) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("code", e.code());
        body.put("message", e.getMessage());
        body.put("status", e.status().value());
        body.put("path", req.getRequestURI());
        body.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.status(e.status()).body(body);
    }

}
