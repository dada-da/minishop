package dev.dada.minishop.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * TASK MS-07: + Bean Validation (@NotBlank, @Positive, @PositiveOrZero).
 */

@Getter
public class ProductRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @Positive
    @DecimalMin("0")
    private  BigDecimal price;

    @Positive
    private BigDecimal originalPrice;

    @Positive
    private int stockQuantity;

    private Long categoryId;
}
