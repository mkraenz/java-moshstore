package eu.kraenz.moshstore.orders.checkout;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import eu.kraenz.moshstore.auth.AuthService;
import eu.kraenz.moshstore.entities.Order;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.orders.OrderRepository;
import eu.kraenz.moshstore.repositories.CartRepository;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class CheckoutService {
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;

  @Value("${websiteUrl}")
  private String websiteUrl;

  @Transactional
  public CheckoutResponseDto handleCheckout(CheckoutDto inputDto)
      throws CannotCheckOutEmptyCart, CartNotFound, StripeException {
    var cart =
        cartRepository
            .findById(UUID.fromString(inputDto.getCartId()))
            .orElseThrow(CartNotFound::new);
    if (cart.isEmpty()) throw new CannotCheckOutEmptyCart();

    var user = authService.findCurrentUser();
    var order = Order.fromCart(cart, user);
    orderRepository.save(order);

    try {
      var session = createStripeSession(order);
      cart.clear();
      cartRepository.save(cart);
      CheckoutResponseDto dto = new CheckoutResponseDto(order.getId(), session.getUrl());
      return dto;
    } catch (StripeException e) {
      orderRepository.delete(order);
      throw e;
    }
  }

  private Session createStripeSession(Order order) throws StripeException {
    final String currency = "eur";
    final int euroToCents = 100;
    var paramBuilder =
        SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(websiteUrl + "/checkout-success.html?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel");

    var lineItems =
        order.getItems().stream()
            .map(
                item ->
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmountDecimal(
                                    item.getUnitPrice().multiply(BigDecimal.valueOf(euroToCents)))
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getProduct().getName())
                                        .build())
                                .build())
                        .build())
            .toList();
    paramBuilder.addAllLineItem(lineItems);
    var session = Session.create(paramBuilder.build());
    return session;
  }
}
