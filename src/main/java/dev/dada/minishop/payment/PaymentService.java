package dev.dada.minishop.payment;

import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.order.Order;
import dev.dada.minishop.order.OrderRepository;
import dev.dada.minishop.payment.dto.PaymentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {
    // TODO MS-20
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createPending(Long orderId, BigDecimal amount, String key, String method) {
        Payment newPayment = new Payment();
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BusinessException("Order Not Found"));

        newPayment.setAmount(amount);
        newPayment.setIdempotencyKey(key);
        newPayment.setStatus(PaymentStatus.PENDING);
        newPayment.setMethod(method);
        newPayment.setOrder(order);

        paymentRepository.save(newPayment);
    }

    @Transactional
    public Payment updatePending(String key, String transactionId,boolean success) {
        Payment payment = paymentRepository.findByIdempotencyKey(key).orElseThrow(() -> new BusinessException("Payment Not Found"));
        payment.setTransactionId(transactionId);

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }

    public PaymentDto toResponseDto(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getStatus(), payment.getAmount());
    }
}
