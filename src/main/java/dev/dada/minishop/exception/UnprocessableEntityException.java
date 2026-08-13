package dev.dada.minishop.exception;

public class UnprocessableEntityException extends BusinessException {
    public UnprocessableEntityException(String message) {
        super(message);
    }
}
