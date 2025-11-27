package eu.kraenz.moshstore.orders.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebhookRequest {
  private String signature;
  private String payload;
}
