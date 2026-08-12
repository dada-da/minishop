package dev.dada.minishop.exception;

import dev.dada.minishop.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TASK MS-24: Bat tat ca exception -> tra ve ErrorResponse dong nhat.
 * - ResourceNotFoundException  -> 404
 * - BusinessException          -> 409 / 400
 * - MethodArgumentNotValidException (validation) -> 400 + danh sach loi field
 * - AccessDeniedException      -> 403
 * - Exception (fallback)       -> 500
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // TODO MS-24: @ExceptionHandler cho tung loai

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = PaymentGatewayTimeoutException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handlePaymentGatewayTimeoutException(PaymentGatewayTimeoutException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(value = InvalidPaymentTokenException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleInvalidPaymentTokenException(InvalidPaymentTokenException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessArgumentException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnwantedException(Exception exception, HttpServletRequest request) {
        Instant now = Instant.now();

        String message = "Unknown Error ID:" + UUID.randomUUID();

        log.error("{}{}{}", now.toString(), message, exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(now, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message, request.getRequestURI(), null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(errorResponse));
    }

    private Map<String, String> toErrorMap(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""),
                                (existing, replacement) -> existing + ", " + replacement
                        )
                );
    }

    private ErrorResponse getErrorResponse(Exception exception, HttpServletRequest request, HttpStatus httpStatus) {
        Instant now = Instant.now();

        Map<String, String> fieldErrors = null;

        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            fieldErrors = toErrorMap(methodArgumentNotValidException.getBindingResult().getFieldErrors());
        }

        return new ErrorResponse(now, httpStatus.value(), null, exception.getMessage(), request.getRequestURI(), fieldErrors);
    }

    private ResponseEntity<ApiResponse<ErrorResponse>> getResponseEntity(Exception exception, HttpServletRequest request, HttpStatus httpStatus) {
        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = getErrorResponse(exception, request, httpStatus);

        return ResponseEntity.status(httpStatus).body(ApiResponse.error(errorResponse));
    }
}
