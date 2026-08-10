package dev.dada.minishop.payment;

import dev.dada.minishop.exception.BusinessException;
import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.UUID;

@Component
@Profile({"test", "local", "dev"})
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public PaymentResponse charge(PaymentRequest request) throws SocketTimeoutException {
        if (request.paymentToken().equals("tok_declined")) {
            String id = "MOCK-FAIL-TXN-" + UUID.randomUUID();

            return new PaymentResponse(id, false, "Declined payment token test");
        }

        if (request.paymentToken().equals("tok_timeout")) {
            throw new SocketTimeoutException("Timeout");
        }

        if (request.paymentToken().equals("tok_success")) {
            String id = "MOCK-TXN-" + UUID.randomUUID();

            return new PaymentResponse(id, true, null);
        }

        String id = "MOCK-UNKNOW-ERROR-" + UUID.randomUUID();

        return new PaymentResponse(id, false, "Unknown error payment test");
    }

    @Override
    public PaymentResponse refund(String id, BigDecimal amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
