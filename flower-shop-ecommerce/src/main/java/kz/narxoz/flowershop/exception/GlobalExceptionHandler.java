package kz.narxoz.flowershop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError(Instant.now(), 404, "NOT_FOUND", ex.getMessage(), req.getRequestURI())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(Instant.now(), 400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(Instant.now(), 500, "INTERNAL_SERVER_ERROR", ex.getMessage(), req.getRequestURI())
        );
    }
}
