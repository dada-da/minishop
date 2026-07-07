package dev.dada.minishop.cart;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TASK MS-14: Entity Cart (user @OneToOne, items @OneToMany cascade).
 */
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {
    // TODO MS-14

    @Column(name = "user_id",nullable = false)
    private Long userId;
}
