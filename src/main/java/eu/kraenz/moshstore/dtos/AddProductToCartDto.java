package eu.kraenz.moshstore.dtos;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddProductToCartDto {
  @Min(1)
  private Long productId;
}
