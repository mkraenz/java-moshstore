package eu.kraenz.moshstore.carts;

import eu.kraenz.moshstore.app.ErrorResponseDto;
import eu.kraenz.moshstore.common.exceptions.CartNotFound;
import eu.kraenz.moshstore.common.httpErrors.CustomHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carts")
public class CartController {
  private final CartService cartService;

  @GetMapping("/{id}")
  @Operation(summary = "Retrieve a cart")
  @ApiResponse(responseCode = "200", description = "Success.")
  public ResponseEntity<CartDto> findOne(@PathVariable("id") UUID id) {
    var cart = cartService.findOne(id);
    return ResponseEntity.ok(cart);
  }

  @PostMapping
  @Operation(summary = "Create an empty cart.")
  public ResponseEntity<CartDto> create(UriComponentsBuilder uriBuilder) {
    var cart = cartService.create();
    var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart).toUri();
    return ResponseEntity.created(uri).body(cart);
  }

  @PostMapping("{id}/items")
  @Operation(summary = "Add a product to a cart.")
  public ResponseEntity<?> addItem(
      @Parameter(description = "The ID of the cart.") @PathVariable("id") UUID id,
      @Valid @RequestBody AddProductToCartDto inputDto) {
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
  public ResponseEntity<ErrorResponseDto> handleCartNotFound() {
    return CustomHttpResponse.resourceNotFound("cart");
  }

  @ExceptionHandler(CartItemNotFound.class)
  public ResponseEntity<ErrorResponseDto> handleCartItemNotFound() {
    return CustomHttpResponse.resourceNotFound("cart item");
  }
}
