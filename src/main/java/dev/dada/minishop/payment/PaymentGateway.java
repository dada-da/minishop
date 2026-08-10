package dev.dada.minishop.payment;

import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;

public interface PaymentGateway {
    PaymentResponse charge(PaymentRequest paymentRequest) throws SocketTimeoutException;

    PaymentResponse refund(String id, BigDecimal amount);
}
