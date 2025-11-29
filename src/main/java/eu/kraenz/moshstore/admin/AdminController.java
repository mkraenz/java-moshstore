package eu.kraenz.moshstore.admin;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/admin")
class AdminController {
  @GetMapping("/hello")
  @Operation(hidden = true)
  public String sayHello() {
    return "Hello admin";
  }
}
