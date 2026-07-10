package dev.dada.minishop.product;

import dev.dada.minishop.category.Category;
import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.dialect.pagination.FetchLimitHandler;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * TASK MS-06: Entity Product (name, description, price BigDecimal, stockQuantity, category @ManyToOne).
 * TASK MS-21: them @Version (Long) cho optimistic locking - chong oversell.
 */
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    @Positive
    @DecimalMin("0")
    private BigDecimal price;

    @Column(name = "original_price")
    @Positive
    @DecimalMin("0")
    private BigDecimal originalPrice;

    @Column(name = "stock_quantity")
    @PositiveOrZero
    private Integer stockQuantity;

    @Column(name = "version", nullable = false)
    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    // TODO MS-06: cac field
    // TODO MS-21: @Version private Long version;
}
