package dev.dada.minishop.cart;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TASK MS-14: Entity CartItem (cart @ManyToOne, product @ManyToOne, quantity).
 */
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    // TODO MS-14
}
