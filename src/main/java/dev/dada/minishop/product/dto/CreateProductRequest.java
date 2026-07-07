package dev.dada.minishop.product.dto;

import java.math.BigDecimal;

/**
 * TASK MS-07: + Bean Validation (@NotBlank, @Positive, @PositiveOrZero).
 */
public record CreateProductRequest(
        String name, String description,
        BigDecimal price, Integer stockQuantity, Long categoryId
) {
}
