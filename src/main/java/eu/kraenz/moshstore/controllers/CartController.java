package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.AddProductToCartDto;
import eu.kraenz.moshstore.dtos.CartDto;
import eu.kraenz.moshstore.entities.Cart;
import eu.kraenz.moshstore.entities.CartItem;
import eu.kraenz.moshstore.mappers.CartItemMapper;
import eu.kraenz.moshstore.mappers.CartMapper;
import eu.kraenz.moshstore.repositories.CartItemRepository;
import eu.kraenz.moshstore.repositories.CartRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
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
  private final CartRepository cartRepository;
  private final CartMapper cartMapper;
  private final CartItemRepository cartItemRepository;
  private final CartItemMapper cartItemMapper;
  private final ProductRepository productRepository;

  @PostMapping
  public ResponseEntity<CartDto> create(UriComponentsBuilder uriBuilder) {
    var cart = new Cart();
    cartRepository.save(cart);
    var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart).toUri();
    return ResponseEntity.created(uri).body(cartMapper.toDto(cart));
  }

  @PostMapping("{id}/items")
  public ResponseEntity<?> addItem(
      @PathVariable(name = "id") UUID id, @RequestBody AddProductToCartDto inputDto) {
    var cart = cartRepository.findById(id).orElse(null);
    if (cart == null) {
      return ResponseEntity.notFound().build();
    }
    var product = productRepository.findById(inputDto.getProductId()).orElse(null);
    if (product == null) {
      return ResponseEntity.badRequest().body(Map.of("errorMessage", "Product does not exist."));
    }
    var item =
        cart.getItems().stream()
            .filter(i -> i.getProduct().getId() == inputDto.getProductId())
            .findFirst()
            .orElse(CartItem.builder().cart(cart).product(product).quantity(0).build());
    item.setQuantity(item.getQuantity() + 1);
    cartItemRepository.save(item);
    return ResponseEntity.ok(cartItemMapper.toDto(item));
  }
}
