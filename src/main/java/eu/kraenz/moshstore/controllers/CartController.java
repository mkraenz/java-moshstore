package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.AddProductToCartDto;
import eu.kraenz.moshstore.dtos.CartDto;
import eu.kraenz.moshstore.dtos.CartItemDto;
import eu.kraenz.moshstore.dtos.UpdateCartItem;
import eu.kraenz.moshstore.entities.Cart;
import eu.kraenz.moshstore.entities.CartItem;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.mappers.CartMapper;
import eu.kraenz.moshstore.repositories.CartItemRepository;
import eu.kraenz.moshstore.repositories.CartRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
  private final CartRepository cartRepository;
  private final CartMapper cartMapper;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;

  @GetMapping("/{id}")
  public ResponseEntity<CartDto> findOne(@PathVariable UUID id) {
    var cart = cartRepository.findWithItems(id).orElse(null);
    if (cart == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(cartMapper.toCartDto(cart));
  }

  @PostMapping
  public ResponseEntity<CartDto> create(UriComponentsBuilder uriBuilder) {
    var cart = new Cart();
    cartRepository.save(cart);
    var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart).toUri();
    return ResponseEntity.created(uri).body(cartMapper.toCartDto(cart));
  }

  @PostMapping("{id}/items")
  public ResponseEntity<?> addItem(
      @PathVariable(name = "id") UUID id, @Valid @RequestBody AddProductToCartDto inputDto) {
    var cart = cartRepository.findById(id).orElse(null);
    if (cart == null) {
      return CustomHttpResponse.resourceNotFound("Cart item");
    }
    var product = productRepository.findById(inputDto.getProductId()).orElse(null);
    if (product == null) {
      return ResponseEntity.badRequest().body(Map.of("errorMessage", "Product does not exist."));
    }
    var item =
        cart.findItem(inputDto.getProductId())
            .orElse(CartItem.builder().cart(cart).product(product).quantity(0).build());
    item.setQuantity(item.getQuantity() + 1);
    cartItemRepository.save(item);
    return ResponseEntity.ok(cartMapper.toCartItemDto(item));
  }

  @PutMapping("/{id}/items/{productId}")
  public ResponseEntity<CartItemDto> updateCartItem(
      @PathVariable(name = "id") UUID cartId,
      @PathVariable(name = "productId") Long productId,
      @Valid @RequestBody UpdateCartItem inputDto) {
    var cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null) {
      return CustomHttpResponse.resourceNotFound("Cart");
    }
    var item = cart.findItemOrNull(productId);
    if (item == null) {
      return CustomHttpResponse.resourceNotFound("Cart item");
    }
    item.setQuantity(inputDto.getQuantity());
    cartItemRepository.save(item);
    return ResponseEntity.ok(cartMapper.toCartItemDto(item));
  }

  @DeleteMapping("/{id}/items/{productId}")
  public ResponseEntity<Void> removeCartItem(
      @PathVariable(name = "id") UUID cartId, @PathVariable(name = "productId") Long productId) {
    var cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null) {
      return CustomHttpResponse.resourceNotFound("Cart");
    }
    var item = cart.findItemOrNull(productId);
    if (item == null) {
      return CustomHttpResponse.resourceNotFound("Cart item");
    }
    cartItemRepository.delete(item);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/items")
  public ResponseEntity<Void> clearCart(@PathVariable(name = "id") UUID cartId) {
    var cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null) {
      return CustomHttpResponse.resourceNotFound("Cart");
    }
    cart.clear();
    cartRepository.save(cart);
    return ResponseEntity.noContent().build();
  }
}
