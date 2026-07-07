package dev.dada.minishop.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ===== TRAI TIM CUA PROJECT - TASK MS-18 + MS-21 =====
 *
 * placeOrder(userId):
 *   1. Lay cart cua user, validate khong rong
 *   2. Voi moi item: kiem tra ton kho, tru stockQuantity
 *   3. Tao Order + OrderItem (snapshot gia)
 *   4. Tao Payment (PENDING)
 *   5. Xoa cart
 *   => TAT CA trong 1 @Transactional. Payment fail -> rollback tru kho.
 *
 * MS-21 (concurrency): khi tru kho, @Version tren Product gay
 * OptimisticLockException neu 2 nguoi mua cung luc -> bat va bao "het hang/thu lai".
 */
@Service
public class OrderService {

    @Transactional
    public void placeOrder(Long userId) {
        // TODO MS-18, MS-21
    }
}
