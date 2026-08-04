package dev.dada.minishop.order;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

/**
 * TASK MS-17: OrderItem (order, productId, productName snapshot, unitPrice snapshot, quantity).
 */
@Entity
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

    @Column(name = "unit_price", nullable = false)
    @Positive
    private BigInteger unitPrice;

    @Column(name = "unit_original_price", nullable = false)
    @Positive
    private BigInteger unitOriginalPrice;
}
