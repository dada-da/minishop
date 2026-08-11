package dev.dada.minishop.exception;

public class InvalidPaymentTokenException extends RuntimeException {
    public InvalidPaymentTokenException(String message) {
        super(message);
    }
}
