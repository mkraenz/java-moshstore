package eu.kraenz.moshstore.orders.checkout;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import eu.kraenz.moshstore.auth.AuthService;
import eu.kraenz.moshstore.entities.Order;
import eu.kraenz.moshstore.entities.PaymentStatus;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.orders.OrderRepository;
import eu.kraenz.moshstore.repositories.CartRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class CheckoutService {
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final PaymentGateway paymentGateway;

  @Value("${websiteUrl}")
  private String websiteUrl;

  @Transactional
  public CheckoutResponseDto handleCheckout(CheckoutDto inputDto)
      throws CannotCheckOutEmptyCart, CartNotFound, PaymentException {
    var cart =
        cartRepository
            .findById(UUID.fromString(inputDto.getCartId()))
            .orElseThrow(CartNotFound::new);
    if (cart.isEmpty()) throw new CannotCheckOutEmptyCart();

    var user = authService.findCurrentUser();
    var order = Order.fromCart(cart, user);
    orderRepository.save(order);

    try {
      var session = paymentGateway.createCheckoutSession(order, websiteUrl);
      cart.clear();
      cartRepository.save(cart);
      return new CheckoutResponseDto(order.getId(), session.getRedirectUrl());
    } catch (PaymentException e) {
      orderRepository.delete(order);
      throw e;
    }
  }

  public void handleWebhookEvent(WebhookRequest request) {
    paymentGateway
        .parseWebhookRequest(request)
        .ifPresent(
            result -> {
              var order = orderRepository.findById(result.getOrderId()).orElseThrow();
              order.setStatus(result.getPaymentStatus());
              orderRepository.save(order);
            });
  }
}
