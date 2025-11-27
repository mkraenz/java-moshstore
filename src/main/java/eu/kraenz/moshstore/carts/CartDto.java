package eu.kraenz.moshstore.carts;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
  private UUID id;
  private List<CartItemDto> items;
  private BigDecimal totalPrice;
}
