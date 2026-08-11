package dev.dada.minishop.checkout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest (@NotNull Long orderId , @NotBlank String idempotencyKey, @NotBlank String paymentToken, String method) {
}