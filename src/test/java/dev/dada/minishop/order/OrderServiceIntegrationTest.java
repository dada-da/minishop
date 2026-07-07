package dev.dada.minishop.order;

/**
 * TASK MS-27: Integration test luong dat hang voi Testcontainers (Postgres that).
 * Cac case can co:
 *  - placeOrder thanh cong -> stock giam dung, order PAID/PENDING, cart bi xoa
 *  - cart rong -> nem BusinessException
 *  - stock khong du -> nem BusinessException, KHONG tru kho (kiem tra rollback)
 */
class OrderServiceIntegrationTest {
    // TODO MS-27
}
