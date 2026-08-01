package dev.dada.minishop.cart;

import dev.dada.minishop.cart.dto.AddToCartRequest;
import dev.dada.minishop.cart.dto.CartDto;
import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.user.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** TASK MS-15: /api/cart (CUSTOMER). */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    // TODO MS-15
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping()
    public ApiResponse<CartDto> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(cartService.getCart(userDetails.getUserId()));
    }

    @PostMapping()
    public ApiResponse<CartDto> addToCart(@Valid @RequestBody AddToCartRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(cartService.addCartItem(request, userDetails.getUserId()));
    }
}
