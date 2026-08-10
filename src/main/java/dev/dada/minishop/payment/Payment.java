package dev.dada.minishop.payment;

import dev.dada.minishop.common.BaseEntity;
import dev.dada.minishop.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * TASK MS-20: Payment (order @OneToOne, amount, status, method). Mock - khong tich hop cong that.
 */
@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment extends BaseEntity {
    // TODO MS-20
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "method")
    private String method;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;
}
