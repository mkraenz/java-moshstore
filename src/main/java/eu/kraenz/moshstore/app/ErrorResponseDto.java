package eu.kraenz.moshstore.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponseDto {
  @Schema(description = "The HTTP status as a string", example = "EXAMPLE_ERROR")
  public String error;

  @Schema(description = "The HTTP status code", example = "123")
  public Integer statusCode;

  @Schema(description = "A description of the error.", example = "Example error message.")
  public String message;

  @Schema(
      description = "An optional object with further details about the error.",
      required = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Map<String, Object> details;
}
