package dev.dada.minishop.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * TASK MS-24: Bat tat ca exception -> tra ve ErrorResponse dong nhat.
 * - ResourceNotFoundException  -> 404
 * - BusinessException          -> 409 / 400
 * - MethodArgumentNotValidException (validation) -> 400 + danh sach loi field
 * - AccessDeniedException      -> 403
 * - Exception (fallback)       -> 500
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO MS-24: @ExceptionHandler cho tung loai
}
