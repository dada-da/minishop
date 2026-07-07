package dev.dada.minishop.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(Long id, String status, BigDecimal totalAmount,
                       List<OrderItemDto> items) {
}
