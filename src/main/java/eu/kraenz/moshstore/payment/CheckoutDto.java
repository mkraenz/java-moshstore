package eu.kraenz.moshstore.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Schema(description = "Request body for checking out a cart and creating a corresponding order.")
public class CheckoutDto {
  @Schema(description = "The ID of the cart.")
  @NotBlank
  @UUID
  private String cartId;
}
