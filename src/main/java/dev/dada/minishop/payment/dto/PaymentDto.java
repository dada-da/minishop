package dev.dada.minishop.payment.dto;

import dev.dada.minishop.payment.PaymentStatus;

import java.math.BigDecimal;

public record PaymentDto (Long id, PaymentStatus status, BigDecimal amount, String message) {
}
