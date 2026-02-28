package com.karandashov.monolith.configuration.web;

import com.karandashov.monolith.dto.response.ErrorResponse;
import com.karandashov.monolith.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.karandashov.monolith.configuration.web.TraceIdFilter.MDC_TRACE_ID;
import static com.karandashov.monolith.exception.Error.UNEXPECTED_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", message);
        ErrorResponse response = new ErrorResponse(4000, "Невалидные данные: " + message, MDC.get(MDC_TRACE_ID));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        log.error("Handling BaseException:", ex);
        ErrorResponse response = new ErrorResponse(ex.getError().getErrorCode(), ex.getError().getMessage(), MDC.get(MDC_TRACE_ID));
        return ResponseEntity.status(ex.getError().getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Handling unexpected exception: ", ex);
        ErrorResponse response = new ErrorResponse(UNEXPECTED_ERROR.getErrorCode(), UNEXPECTED_ERROR.getMessage(), MDC.get(MDC_TRACE_ID));
        return ResponseEntity.status(UNEXPECTED_ERROR.getHttpStatusCode()).body(response);
    }
}