package dev.dada.minishop.exception;

public class PaymentGatewayTimeoutException extends RuntimeException {
    public PaymentGatewayTimeoutException(String message) {
        super(message);
    }
}
