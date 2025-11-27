package eu.kraenz.moshstore.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutSession {
  private String redirectUrl;
}
