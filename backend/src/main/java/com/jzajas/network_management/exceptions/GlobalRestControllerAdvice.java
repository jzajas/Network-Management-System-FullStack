package com.jzajas.network_management.exceptions;

import com.jzajas.network_management.dtos.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleDeviceNotFound(
            DeviceNotFoundException ex
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Device with id " + ex.getDeviceId() + " does not exist"
                );
    }

    @ExceptionHandler(InvalidDeviceStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDeviceState(
            InvalidDeviceStateException ex
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Device " + ex.getId() +
                        " is already in state: " + ex.isActive()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .findFirst()
                .orElse(VALIDATION_FAILED_MESSAGE);

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncException(AsyncRequestNotUsableException ex) {
        log.debug("SSE client disconnected: {}", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Failed to send")) {
            log.debug("SSE send failed: {}", ex.getMessage());
            return null;
        }
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(
            Exception ex
    ) {
        log.error("Exception caught: ", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                UNEXPECTED_ERROR_MESSAGE
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status, String message
    ) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorResponse(
                        message,
                        Instant.now()
                        )
                );
    }
}
