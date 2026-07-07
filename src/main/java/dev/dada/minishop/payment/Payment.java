package dev.dada.minishop.payment;

import dev.dada.minishop.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TASK MS-20: Payment (order @OneToOne, amount, status, method). Mock - khong tich hop cong that.
 */
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {
    // TODO MS-20
}
