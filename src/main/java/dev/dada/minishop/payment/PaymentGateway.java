package dev.dada.minishop.payment;

import dev.dada.minishop.exception.PaymentGatewayTimeoutException;
import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentResponse charge(PaymentRequest paymentRequest) throws PaymentGatewayTimeoutException;

    PaymentResponse refund(String id, BigDecimal amount);
}
