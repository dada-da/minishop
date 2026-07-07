package dev.dada.minishop.product;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * TASK MS-06: Entity Product (name, description, price BigDecimal, stockQuantity, category @ManyToOne).
 * TASK MS-21: them @Version (Long) cho optimistic locking - chong oversell.
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    // TODO MS-06: cac field
    // TODO MS-21: @Version private Long version;
}
