package dev.dada.minishop.exception;

/**
 * TASK MS-24: Nem khi khong tim thay entity (product, order...).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
