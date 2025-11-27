package eu.kraenz.moshstore.carts;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemProductDto {
  private Long id;
  private String name;
  private BigDecimal price;
}
