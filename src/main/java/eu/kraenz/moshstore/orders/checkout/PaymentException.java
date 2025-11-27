package eu.kraenz.moshstore.orders.checkout;

import lombok.NoArgsConstructor;

@NoArgsConstructor
class PaymentException extends RuntimeException {
  PaymentException(String message) {
    super(message);
  }
}
