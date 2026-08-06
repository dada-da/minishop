package dev.dada.minishop.order.dto;

import dev.dada.minishop.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest (@NotNull OrderStatus status) {
}
