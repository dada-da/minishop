package dev.dada.minishop.cart.dto;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(@Positive Integer quantity) {
}
