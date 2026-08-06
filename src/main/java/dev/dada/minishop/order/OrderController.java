package dev.dada.minishop.order;

import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.common.PageResponse;
import dev.dada.minishop.order.dto.ChangeStatusRequest;
import dev.dada.minishop.order.dto.OrderDto;
import dev.dada.minishop.user.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderDto> placeOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(orderService.placeOrder(userDetails.getUserId()));
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderDto>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String direction,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(orderService.getOrders(page, size, sortBy, direction, userDetails.getUserId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDto> getOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(orderService.getOrderById(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<OrderDto> changeOrderStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(orderService.changeOrderStatus(id, request, userDetails.getUserId()));
    }
}
