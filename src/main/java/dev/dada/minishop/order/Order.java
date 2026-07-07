package dev.dada.minishop.order;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.*;

/**
 * TASK MS-17: Entity Order (user, items @OneToMany, status, totalAmount, snapshot dia chi).
 * Luu y: OrderItem phai snapshot price tai thoi diem dat (gia product co the doi sau).
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    // TODO MS-17

    @Column(name = "user_id",nullable = false)
    private Long userId;
}
