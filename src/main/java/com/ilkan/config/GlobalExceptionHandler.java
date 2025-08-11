package com.ilkan.config;

import com.ilkan.auth.RoleExceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
}
