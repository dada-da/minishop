package dev.dada.minishop.checkout;

import dev.dada.minishop.checkout.dto.CheckoutRequest;
import dev.dada.minishop.order.OrderService;
import dev.dada.minishop.order.dto.OrderDto;
import dev.dada.minishop.payment.Payment;
import dev.dada.minishop.payment.PaymentGateway;
import dev.dada.minishop.payment.PaymentService;
import dev.dada.minishop.payment.dto.PaymentDto;
import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
public class CheckoutService {
    private final PaymentGateway paymentGateway;
    private final PaymentService paymentService;
    private final OrderService orderService;

    public CheckoutService(PaymentGateway paymentGateway, PaymentService paymentService, OrderService orderService) {
        this.paymentGateway = paymentGateway;
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    public PaymentDto checkout(CheckoutRequest request, Long userId) {
        OrderDto orderResponse = orderService.placeOrder(userId);

        paymentService.createPending(orderResponse.id(), orderResponse.totalAmount(), request.idempotencyKey(), request.method());
        PaymentResponse paymentRes = null;

        try {
            PaymentRequest paymentReq = new PaymentRequest(orderResponse.id(), orderResponse.totalAmount(), request.paymentToken());
            paymentRes = paymentGateway.charge(paymentReq);
        } catch (SocketTimeoutException e) {
            //TODO handle retry
        }

        Payment payment = paymentService.updatePending(request.idempotencyKey(), paymentRes.id(), paymentRes.isSuccess());

        return paymentService.toResponseDto(payment);
    }
}
