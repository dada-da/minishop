package dev.dada.minishop.cart;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * TASK MS-14: Entity CartItem (cart @ManyToOne, product @ManyToOne, quantity).
 */
@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    // TODO MS-14

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Positive
    private Integer quantity;
}
