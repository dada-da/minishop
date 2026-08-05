package dev.dada.minishop.order.dto;

import java.math.BigDecimal;

public record OrderItemDto(Long productId, String productName,
                           int quantity, BigDecimal unitPrice, BigDecimal unitOriginalPrice) {
}
