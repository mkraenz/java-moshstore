package eu.kraenz.moshstore.orders.checkout;

import eu.kraenz.moshstore.auth.AuthService;
import eu.kraenz.moshstore.entities.Order;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.orders.OrderRepository;
import eu.kraenz.moshstore.repositories.CartRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Service
class CheckoutService {
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;

  public CheckoutResponseDto handleCheckout(CheckoutDto inputDto)
      throws CannotCheckOutEmptyCart, CartNotFound {
    var cart =
        cartRepository
            .findById(UUID.fromString(inputDto.getCartId()))
            .orElseThrow(CartNotFound::new);
    if (cart.isEmpty()) throw new CannotCheckOutEmptyCart();

    var user = authService.findCurrentUser();
    var order = Order.fromCart(cart, user);
    orderRepository.save(order);
    cart.clear();
    cartRepository.save(cart);
    CheckoutResponseDto dto = new CheckoutResponseDto(order.getId());
    return dto;
  }
}
