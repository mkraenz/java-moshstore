package eu.kraenz.moshstore.orders.checkout;

import eu.kraenz.moshstore.dtos.ErrorResponseDto;
import eu.kraenz.moshstore.exceptions.CartNotFound;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
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

  @PostMapping
  @Operation(
      summary = "Create an order from a cart.",
      description = "Checks out a cart by creating a corresponding order and emptying the cart.")
  @ApiResponse(responseCode = "200", description = "Checkout successful.")
  public CheckoutResponseDto checkoutAndCreateOrder(@Valid @RequestBody CheckoutDto inputDto) {
    return checkoutService.handleCheckout(inputDto);
  }

  // we accept the specificity to stripe so that request logs immediately show which gateway we need
  // to check
  @PostMapping("/webhooks/stripe")
  @Hidden()
  public ResponseEntity<Void> handleWebhook(
      @RequestHeader("Stripe-Signature") String signature, @RequestBody String payload) {
    checkoutService.handleWebhookEvent(new WebhookRequest(signature, payload));
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(CartNotFound.class)
  public ResponseEntity<ErrorResponseDto> handleCartNotFound() {
    return CustomHttpResponse.badRequest("Cart not found.");
  }

  @ExceptionHandler(CannotCheckOutEmptyCart.class)
  public ResponseEntity<ErrorResponseDto> handleCannotCheckOutEmptyCart() {
    return CustomHttpResponse.badRequest("Cannot check out an empty cart");
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<ErrorResponseDto> handlePaymentException() {
    return CustomHttpResponse.pleaseTryAgain();
  }
}
