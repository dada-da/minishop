package dev.dada.minishop.checkout;

import dev.dada.minishop.checkout.dto.CheckoutRequest;
import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.exception.PaymentGatewayTimeoutException;
import dev.dada.minishop.order.Order;
import dev.dada.minishop.order.OrderService;
import dev.dada.minishop.order.OrderStatus;
import dev.dada.minishop.payment.Payment;
import dev.dada.minishop.payment.PaymentGateway;
import dev.dada.minishop.payment.PaymentService;
import dev.dada.minishop.payment.PaymentStatus;
import dev.dada.minishop.payment.dto.PaymentDto;
import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;


@Service
public class CheckoutService {
    private final TransactionTemplate transactionTemplate;
    private final PaymentGateway paymentGateway;
    private final PaymentService paymentService;
    private final OrderService orderService;

    public CheckoutService(PaymentGateway paymentGateway,
                           PaymentService paymentService,
                           OrderService orderService,
                           TransactionTemplate transactionTemplate) {
        this.paymentGateway = paymentGateway;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.transactionTemplate = transactionTemplate;
    }

    public PaymentDto checkout(CheckoutRequest request, Long userId) {
        Order order = orderService.getOrderEntityById(request.orderId(), userId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Order Not Wait For Payment");
        }

        Payment existingPayment = paymentService.createPending(order.getId(), request.idempotencyKey(), request.method(), order.getTotalAmount());

        if (existingPayment.getStatus() != PaymentStatus.PENDING) {
            return paymentService.toResponseDto(existingPayment, "Payment has been process");
        }

        PaymentResponse paymentRes;

        try {
            PaymentRequest paymentReq = new PaymentRequest(order.getId(), order.getTotalAmount(), request.paymentToken());
            paymentRes = paymentGateway.charge(paymentReq);
        } catch (PaymentGatewayTimeoutException e) {
            return paymentService.toResponseDto(paymentService.markUnknown(request.idempotencyKey()), "Payment Gateway Timeout");
        }


        Payment payment = applyPaymentResult(userId, paymentRes, request);

        return paymentService.toResponseDto(payment, paymentRes.errorMessage());
    }

    private Payment applyPaymentResult(Long userId, PaymentResponse paymentRes, CheckoutRequest request) {
        return transactionTemplate.execute(status -> {
            Payment payment = paymentService.updatePending(request.idempotencyKey(), paymentRes.id(), paymentRes.isSuccess());

            if (paymentRes.isSuccess()) {
                orderService.markPaid(request.orderId(), userId);
            } else {
                orderService.cancelOrder(request.orderId(), userId);
            }

            return payment;
        });
    }
}
