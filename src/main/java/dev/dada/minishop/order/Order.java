package dev.dada.minishop.order;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * TASK MS-17: Entity Order (user, items @OneToMany, status, totalAmount, snapshot dia chi).
 * Luu y: OrderItem phai snapshot price tai thoi diem dat (gia product co the doi sau).
 */
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {
    // TODO MS-17

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "total_original_amount")
    private BigDecimal originalTotalAmount;

    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}
