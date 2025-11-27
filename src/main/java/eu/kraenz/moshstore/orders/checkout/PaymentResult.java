package eu.kraenz.moshstore.orders.checkout;

import eu.kraenz.moshstore.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
  private Long orderId;
  private PaymentStatus paymentStatus;
}
