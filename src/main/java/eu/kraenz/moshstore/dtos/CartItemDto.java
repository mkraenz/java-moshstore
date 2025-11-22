package eu.kraenz.moshstore.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
  private CartItemProductDto product;
  private Integer quantity;
  private BigDecimal totalPrice;
}
