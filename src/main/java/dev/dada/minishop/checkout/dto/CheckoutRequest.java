package dev.dada.minishop.checkout.dto;

public record CheckoutRequest (String idempotencyKey, String paymentToken, String method) {
}