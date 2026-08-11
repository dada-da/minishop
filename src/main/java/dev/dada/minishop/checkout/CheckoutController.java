package dev.dada.minishop.checkout;

import dev.dada.minishop.checkout.dto.CheckoutRequest;
import dev.dada.minishop.common.ApiResponse;
import dev.dada.minishop.payment.dto.PaymentDto;
import dev.dada.minishop.user.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ApiResponse<PaymentDto> checkout(@Valid @RequestBody CheckoutRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(checkoutService.checkout(request, userDetails.getUserId()));
    }
}
