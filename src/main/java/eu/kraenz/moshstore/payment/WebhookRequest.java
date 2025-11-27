package eu.kraenz.moshstore.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebhookRequest {
  private String signature;
  private String payload;
}
