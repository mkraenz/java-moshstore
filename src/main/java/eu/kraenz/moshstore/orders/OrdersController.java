package eu.kraenz.moshstore.orders;

import eu.kraenz.moshstore.auth.AuthService;
import eu.kraenz.moshstore.dtos.ErrorResponseDto;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request. Check the response body for more details."),
      @ApiResponse(responseCode = "401", description = "Unauthenticated."),
    })
class OrdersController {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final AuthService authService;

  @GetMapping
  @Operation(summary = "Retrieve all your orders.")
  public List<OrderDto> findMany() {
    var orders = orderRepository.findByCustomerId(authService.currentUserId());
    return orders.stream().map(o -> orderMapper.toDto(o)).toList();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retrieve your order.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found or not owned by you.",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  public OrderDto findOne(@PathVariable("id") Long id) {
    // For higher security, if the current user does not own the resource, then we return HTTP 404
    // to hide the info that such an order exists.
    var order =
        orderRepository
            .findByIdAndCustomerId(id, authService.currentUserId())
            .orElseThrow(OrderNotFound::new);
    return orderMapper.toDto(order);
  }

  @ExceptionHandler(OrderNotFound.class)
  public ResponseEntity<ErrorResponseDto> handleOrderNotFound() {
    return CustomHttpResponse.resourceNotFound("order");
  }
}
