package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Message;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
// @RequestMapping("/messages")
class MessageController {
  @RequestMapping("/hello")
  public Message sayHello() {
    return new Message("hello world");
  }
}
