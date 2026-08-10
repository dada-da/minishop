package dev.dada.minishop.payment.dto;

public record PaymentResponse(String id, boolean isSuccess, String errorMessage) {
}
