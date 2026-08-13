package dev.dada.minishop.exception;

public class InvalidPaymentTokenException extends BusinessException {
    public InvalidPaymentTokenException(String message) {
        super(message);
    }
}
