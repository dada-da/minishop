package dev.dada.minishop.cart.dto;

import java.math.BigDecimal;

public record CartItemDto(Long productId, String productName,
                          int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
}
