package dev.dada.minishop.payment.dto;

import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PaymentRequest(Long orderId, BigDecimal amount, @Pattern(regexp = "^tok_(declined|timeout|success)$", message = "Token is wrong") String paymentToken) {
}
