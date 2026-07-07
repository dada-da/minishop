package dev.dada.minishop.product.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id, String name, String description,
        BigDecimal price, int stockQuantity, Long categoryId
) {
}
