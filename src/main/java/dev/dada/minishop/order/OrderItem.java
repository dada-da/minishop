package dev.dada.minishop.order;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * TASK MS-17: OrderItem (order, productId, productName snapshot, unitPrice snapshot, quantity).
 */
@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    // TODO MS-17

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Positive
    private Integer quantity;

    @Column(name = "price", nullable = false)
    @Positive
    private BigDecimal unitPrice;

    @Column(name = "original_price")
    @Positive
    private BigDecimal unitOriginalPrice;
}
