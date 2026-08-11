package dev.dada.minishop.payment;

import dev.dada.minishop.exception.InvalidPaymentTokenException;
import dev.dada.minishop.exception.PaymentGatewayTimeoutException;
import dev.dada.minishop.payment.dto.PaymentRequest;
import dev.dada.minishop.payment.dto.PaymentResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Profile({"test", "local", "dev"})
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public PaymentResponse charge(PaymentRequest request) throws PaymentGatewayTimeoutException {
        MockToken requestToken = MockToken.fromValue(request.paymentToken());

        switch (requestToken) {
            case SUCCESS -> {
                String id = "MOCK-TXN-" + UUID.randomUUID();

                return new PaymentResponse(id, true, null);
            }

            case DECLINED -> {
                String id = "MOCK-FAIL-TXN-" + UUID.randomUUID();

                return new PaymentResponse(id, false, "Declined payment token test");
            }

            case TIMEOUT -> throw new PaymentGatewayTimeoutException("Timeout");

            case null -> throw new InvalidPaymentTokenException("Invalid request token");
        }
    }

    @Override
    public PaymentResponse refund(String id, BigDecimal amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
