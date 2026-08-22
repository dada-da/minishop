package dev.dada.minishop.exception;

import dev.dada.minishop.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // TODO MS-24: @ExceptionHandler cho tung loai

    @ExceptionHandler(value = OptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleOptimisticLockingFailureException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            InvalidPaymentTokenException.class,
            ConstraintViolationException.class,
            InvalidOperationException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGeneralBadRequest(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadableException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST, "Malformed or missing JSON request body.");
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST, "Argument Mismatch.");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.warn("{} - {} - {}", HttpStatus.BAD_REQUEST, request.getRequestURI(), exception.getMessage());

        Map<String, String> fieldErrors = toErrorMap(exception.getBindingResult().getFieldErrors());

        ErrorResponse errorResponse = new ErrorResponse("Argument Not Valid", request.getRequestURI(), fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(errorResponse));
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnprocessableEntityException(UnprocessableEntityException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = PaymentGatewayTimeoutException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handlePaymentGatewayTimeoutException(PaymentGatewayTimeoutException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDuplicateResourceException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataIntegrityViolationException(Exception ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.CONFLICT, "Data Is Not Valid");
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnwantedException(Exception exception, HttpServletRequest request) {
        String message = "Unknown Error ID:" + UUID.randomUUID();

        log.error("{} - {}", message, exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(message, request.getRequestURI(), null);

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

    private ErrorResponse getErrorResponse(HttpServletRequest request, String message) {
        return new ErrorResponse(message, request.getRequestURI(), null);
    }

    private void logError(Exception exception, HttpStatus httpStatus) {
        if (httpStatus.is5xxServerError()) {
            log.error(exception.getMessage(), exception);
        } else {
            log.warn("{} - {}", httpStatus, exception.getMessage());
        }
    }

    private ResponseEntity<ApiResponse<ErrorResponse>> getResponseEntity(Exception exception, HttpServletRequest request, HttpStatus httpStatus, String customMessage) {
        logError(exception, httpStatus);

        ErrorResponse errorResponse = getErrorResponse(request, customMessage);

        return ResponseEntity.status(httpStatus).body(ApiResponse.error(errorResponse));
    }

    private ResponseEntity<ApiResponse<ErrorResponse>> getResponseEntity(Exception exception, HttpServletRequest request, HttpStatus httpStatus) {
        logError(exception, httpStatus);

        ErrorResponse errorResponse = getErrorResponse(request, exception.getMessage());

        return ResponseEntity.status(httpStatus).body(ApiResponse.error(errorResponse));
    }
}
