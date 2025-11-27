package eu.kraenz.moshstore.orders.checkout;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import eu.kraenz.moshstore.entities.Order;
import eu.kraenz.moshstore.entities.OrderItem;
import eu.kraenz.moshstore.entities.PaymentStatus;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {
  static final String currency = "eur";

  @Value("${payment.stripe.webhookSecretKey}")
  private String webhookSecret;

  private static SessionCreateParams.LineItem.PriceData.ProductData createProductData(
      OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(item.getProduct().getName())
        .build();
  }

  private static SessionCreateParams.LineItem createLineItem(OrderItem item) {
    return SessionCreateParams.LineItem.builder()
        .setQuantity(Long.valueOf(item.getQuantity()))
        .setPriceData(createPriceData(item))
        .build();
  }

  private static SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency(currency)
        .setUnitAmountDecimal(item.getUnitPriceInEuroCents())
        .setProductData(createProductData(item))
        .build();
  }

  @Override
  public CheckoutSession createCheckoutSession(Order order, String redirectUrl) {
    try {

      var paramBuilder =
          SessionCreateParams.builder()
              .setMode(SessionCreateParams.Mode.PAYMENT)
              .setSuccessUrl(redirectUrl + "/checkout-success.html?orderId=" + order.getId())
              .setCancelUrl(redirectUrl + "/checkout-cancel")
              .putMetadata("order_id", order.getId().toString());

      var lineItems = order.getItems().stream().map(item -> createLineItem(item)).toList();
      paramBuilder.addAllLineItem(lineItems);
      var session = Session.create(paramBuilder.build());
      return new CheckoutSession(session.getUrl());
    } catch (StripeException e) {
      System.out.println("Error creating checkout session. Error message: " + e.getMessage());
      throw new PaymentException();
    }
  }

  @Override
  public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
    try {
      var event =
          Webhook.constructEvent(request.getPayload(), request.getSignature(), webhookSecret);
      return switch (event.getType()) {
        case "payment_intent.succeeded" ->
            Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
        case "payment_intent.payment_failed" ->
            Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
        default -> Optional.empty();
      };
    } catch (SignatureVerificationException e) {
      throw new PaymentException("Invalid signature");
    }
  }

  private Long extractOrderId(Event event) {
    StripeObject stripeObj =
        event.getDataObjectDeserializer().getObject().orElseThrow(IncompatibleStripeSdkAndApi::new);
    var intent = (PaymentIntent) stripeObj;
    return Long.valueOf(intent.getMetadata().get("order_id"));
  }
}
