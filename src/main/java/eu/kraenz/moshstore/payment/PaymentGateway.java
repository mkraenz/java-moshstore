package eu.kraenz.moshstore.payment;

import eu.kraenz.moshstore.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession createCheckoutSession(Order order, String redirectUrl);

  Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
