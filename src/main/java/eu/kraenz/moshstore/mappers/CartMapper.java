package eu.kraenz.moshstore.mappers;

import eu.kraenz.moshstore.dtos.CartDto;
import eu.kraenz.moshstore.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface CartMapper {
  @Mapping(
      target = "totalPrice",
      expression =
          "java(cart.getItems().stream()\n"
              + "        .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))\n"
              + "        .reduce(BigDecimal.ZERO, BigDecimal::add))")
  CartDto toDto(Cart cart);
}
