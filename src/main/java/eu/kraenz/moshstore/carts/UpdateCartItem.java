package eu.kraenz.moshstore.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItem {
  @Max(100)
  @Min(1)
  private int quantity;
}
