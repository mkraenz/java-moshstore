package eu.kraenz.moshstore.orders.checkout;

import com.stripe.exception.StripeException;
import eu.kraenz.moshstore.dtos.ErrorResponseDto;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "Orders")
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request. Check the response body for more details.")
    })
class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping("/checkout")
  @Operation(
      summary = "Create an order from a cart.",
      description = "Checks out a cart by creating a corresponding order and emptying the cart.")
  @ApiResponse(responseCode = "200", description = "Checkout successful.")
  public CheckoutResponseDto checkoutAndCreateOrder(@Valid @RequestBody CheckoutDto inputDto)
      throws StripeException {
    return checkoutService.handleCheckout(inputDto);
  }

  @ExceptionHandler(CartNotFound.class)
  public ResponseEntity<ErrorResponseDto> handleCartNotFound() {
    return CustomHttpResponse.badRequest("Cart not found.");
  }

  @ExceptionHandler(CannotCheckOutEmptyCart.class)
  public ResponseEntity<ErrorResponseDto> handleCannotCheckOutEmptyCart() {
    return CustomHttpResponse.badRequest("Cannot check out an empty cart");
  }

  @ExceptionHandler(StripeException.class)
  public ResponseEntity<ErrorResponseDto> handleStripeException() {
    System.out.println("Error creating Stripe checkout session.");
    return CustomHttpResponse.pleaseTryAgain();
  }
}
