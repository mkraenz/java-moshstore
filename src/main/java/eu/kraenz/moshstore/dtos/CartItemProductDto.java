package eu.kraenz.moshstore.dtos;

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
