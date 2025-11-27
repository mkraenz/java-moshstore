package eu.kraenz.moshstore.payment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
class PaymentException extends RuntimeException {
  PaymentException(String message) {
    super(message);
  }
}
