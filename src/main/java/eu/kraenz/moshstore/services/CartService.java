package eu.kraenz.moshstore.services;

import eu.kraenz.moshstore.dtos.CartDto;
import eu.kraenz.moshstore.dtos.CartItemDto;
import eu.kraenz.moshstore.entities.Cart;
import eu.kraenz.moshstore.entities.CartItem;
import eu.kraenz.moshstore.exceptions.CartItemNotFound;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.exceptions.ProductNotFound;
import eu.kraenz.moshstore.mappers.CartMapper;
import eu.kraenz.moshstore.repositories.CartItemRepository;
import eu.kraenz.moshstore.repositories.CartRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CartService {
  private CartRepository cartRepository;
  private CartMapper cartMapper;
  private ProductRepository productRepository;
  private CartItemRepository cartItemRepository;

  public CartDto create() {
    var cart = new Cart();
    cartRepository.save(cart);
    return cartMapper.toCartDto(cart);
  }

  public CartItemDto addToCart(UUID id, Long productId) {
    var cart = cartRepository.findById(id).orElseThrow(CartNotFound::new);
    var product = productRepository.findById(productId).orElseThrow(ProductNotFound::new);
    var item =
        cart.findItem(productId)
            .orElse(CartItem.builder().cart(cart).product(product).quantity(0).build());
    item.setQuantity(item.getQuantity() + 1);
    cartItemRepository.save(item);
    return cartMapper.toCartItemDto(item);
  }

  public CartItemDto updateItemQuantity(UUID cartId, Long productId, int newQuantity) {
    var cart = cartRepository.findById(cartId).orElseThrow(CartNotFound::new);
    var item = cart.findItem(productId).orElseThrow(CartItemNotFound::new);
    item.setQuantity(newQuantity);
    cartItemRepository.save(item);
    return cartMapper.toCartItemDto(item);
  }

  public void removeFromCart(UUID cartId, Long productId) {
    var cart = cartRepository.findById(cartId).orElseThrow(CartNotFound::new);
    var item = cart.findItem(productId).orElseThrow(CartItemNotFound::new);
    // hibernate un-schedules deleting the child record when there are references left on the parent
    // https://stackoverflow.com/questions/16898085/jpa-hibernate-remove-entity-sometimes-not-working#comment44703936_16901857
    cart.removeItem(item);
    cartItemRepository.deleteById(item.getId());
  }

  public void clearCart(UUID cartId) {
    var cart = cartRepository.findById(cartId).orElseThrow(CartNotFound::new);
    cart.clear();
    cartRepository.save(cart);
  }

  public CartDto findOne(UUID id) {
    var cart = cartRepository.findWithItems(id).orElseThrow(CartItemNotFound::new);
    return cartMapper.toCartDto(cart);
  }
}
