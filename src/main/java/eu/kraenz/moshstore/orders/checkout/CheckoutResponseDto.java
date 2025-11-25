package eu.kraenz.moshstore.orders.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponseDto {
  private Long orderId;
  private String checkoutUrl;
}
