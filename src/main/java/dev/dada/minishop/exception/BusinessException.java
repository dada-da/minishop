package dev.dada.minishop.exception;

/**
 * TASK MS-24: Loi nghiep vu (vd: het hang, gio hang rong, dat trung...).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
