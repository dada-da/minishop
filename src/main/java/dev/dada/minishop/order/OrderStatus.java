package dev.dada.minishop.order;

/** TASK MS-17: trang thai don. */
public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    CANCELLED,
    COMPLETED;

    public boolean canTransitionTo (OrderStatus target) {
        return switch (this) {
            case PENDING -> target == PAID || target == CANCELLED;
            case PAID -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
