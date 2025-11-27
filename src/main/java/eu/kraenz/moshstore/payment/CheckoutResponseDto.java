package eu.kraenz.moshstore.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponseDto {
  private Long orderId;
  private String checkoutUrl;
}
