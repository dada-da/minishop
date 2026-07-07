package dev.dada.minishop.order;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TASK MS-17: OrderItem (order, productId, productName snapshot, unitPrice snapshot, quantity).
 */
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    // TODO MS-17
}
