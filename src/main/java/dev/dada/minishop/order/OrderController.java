package dev.dada.minishop.order;

import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.order.dto.OrderDto;
import dev.dada.minishop.user.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TASK MS-19: POST /api/orders (checkout), GET /api/orders (lich su cua user),
 * GET /api/orders/{id}. Admin: PATCH /api/orders/{id}/status.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    // TODO MS-19
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ApiResponse<Void> placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.placeOrder(userDetails.getUserId());

        return ApiResponse.ok();
    }
}
