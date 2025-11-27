package eu.kraenz.moshstore.carts;

import eu.kraenz.moshstore.entities.Cart;
import eu.kraenz.moshstore.entities.CartItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface CartMapper {
  @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
  // Note: bc the key is identical we actually dont need an explict @Mapping
  @Mapping(target = "items", source = "items")
  CartDto toCartDto(Cart cart);

  @Mapping(target = "totalPrice", expression = "java(item.getTotalPrice())")
  CartItemDto toCartItemDto(CartItem item);
}
