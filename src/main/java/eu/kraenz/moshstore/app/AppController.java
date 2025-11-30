package eu.kraenz.moshstore.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class AppController {
  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("openApiUiUrl", "/docs");
    return "index";
  }
}
