package eu.kraenz.moshstore.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/admin")
class AdminController {
  @GetMapping("/hello")
  public String sayHello() {
    return "Hello admin";
  }
}
