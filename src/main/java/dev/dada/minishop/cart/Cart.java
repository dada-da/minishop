package dev.dada.minishop.cart;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TASK MS-14: Entity Cart (user @OneToOne, items @OneToMany cascade).
 */

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {
    // TODO MS-14

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;
}
