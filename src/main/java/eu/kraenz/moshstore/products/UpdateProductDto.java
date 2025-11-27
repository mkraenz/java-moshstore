package eu.kraenz.moshstore.products;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateProductDto {
  private String name;
  private String description;
  private BigDecimal price;
  private Byte categoryId;
}
