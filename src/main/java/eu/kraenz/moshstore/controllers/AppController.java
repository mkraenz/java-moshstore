package eu.kraenz.moshstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class AppController {
  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("name", "Peter");
    return "index";
  }
}
