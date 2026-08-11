package dev.dada.minishop.payment;

import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.order.Order;
import dev.dada.minishop.order.OrderRepository;
import dev.dada.minishop.payment.dto.PaymentDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment createPending(Long orderId, String key, String method, BigDecimal amount) {
        Optional<Payment> payment = paymentRepository.findByIdempotencyKey(key);

        if (payment.isPresent()) {
            if (Objects.equals(payment.get().getIdempotencyKey(), key) && !orderId.equals(payment.get().getOrderId())) {
                throw new BusinessException("Order has already been paid");
            }

            return payment.get();
        }

        Payment newPayment = new Payment();

        newPayment.setAmount(amount);
        newPayment.setIdempotencyKey(key);
        newPayment.setStatus(PaymentStatus.PENDING);
        newPayment.setMethod(method);
        newPayment.setOrderId(orderId);

        try {
            return paymentRepository.save(newPayment);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("Order has already been pending");
        }
    }

    @Transactional
    public Payment updatePending(String key, String transactionId, boolean success) {
        Payment payment = paymentRepository.findByIdempotencyKey(key).orElseThrow(() -> new BusinessException("Payment Not Found"));

        payment.setTransactionId(transactionId);

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return payment;
    }

    public PaymentDto toResponseDto(Payment payment, String message) {
        return new PaymentDto(payment.getId(), payment.getStatus(), payment.getAmount(), message);
    }

    @Transactional
    public Payment markUnknown(String key) {
        Payment payment = paymentRepository.findByIdempotencyKey(key).orElseThrow(() -> new BusinessException("Payment Not Found"));

        payment.setStatus(PaymentStatus.UNKNOWN);

        return payment;
    }
}
