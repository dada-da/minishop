package dev.dada.minishop.product.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id, String name, String description,
        BigDecimal price, BigDecimal originalPrice, int stockQuantity, Long categoryId
) {
}
