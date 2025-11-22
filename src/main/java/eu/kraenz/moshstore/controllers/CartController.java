package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.AddProductToCartDto;
import eu.kraenz.moshstore.dtos.CartDto;
import eu.kraenz.moshstore.dtos.CartItemDto;
import eu.kraenz.moshstore.dtos.UpdateCartItem;
import eu.kraenz.moshstore.exceptions.CartItemNotFound;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.exceptions.ProductNotFound;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.services.CartService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
  private final CartService cartService;

  @GetMapping("/{id}")
  public ResponseEntity<CartDto> findOne(@PathVariable("id") UUID id) {
    var cart = cartService.findOne(id);
    return ResponseEntity.ok(cart);
  }

  @PostMapping
  public ResponseEntity<CartDto> create(UriComponentsBuilder uriBuilder) {
    var cart = cartService.create();
    var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart).toUri();
    return ResponseEntity.created(uri).body(cart);
  }

  @PostMapping("{id}/items")
  public ResponseEntity<?> addItem(
      @PathVariable("id") UUID id, @Valid @RequestBody AddProductToCartDto inputDto) {
    try {
      var item = cartService.addToCart(id, inputDto.getProductId());
      return ResponseEntity.ok(item);
    } catch (ProductNotFound e) {
      return ResponseEntity.badRequest().body(Map.of("message", "Product does not exist."));
    }
  }

  @PutMapping("/{id}/items/{productId}")
  public ResponseEntity<CartItemDto> updateCartItem(
      @PathVariable("id") UUID cartId,
      @PathVariable("productId") Long productId,
      @Valid @RequestBody UpdateCartItem inputDto) {
    var item = cartService.updateItemQuantity(cartId, productId, inputDto.getQuantity());
    return ResponseEntity.ok(item);
  }

  @DeleteMapping("/{id}/items/{productId}")
  public ResponseEntity<Void> removeCartItem(
      @PathVariable("id") UUID cartId, @PathVariable("productId") Long productId) {
    cartService.removeFromCart(cartId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/items")
  public ResponseEntity<Void> clearCart(@PathVariable("id") UUID cartId) {
    cartService.clearCart(cartId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(CartNotFound.class)
  public ResponseEntity<Map<String, String>> handleCartNotFound() {
    return CustomHttpResponse.resourceNotFound("Cart");
  }

  @ExceptionHandler(CartItemNotFound.class)
  public ResponseEntity<Map<String, String>> handleCartItemNotFound() {
    return CustomHttpResponse.resourceNotFound("Cart item");
  }
}
