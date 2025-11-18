package eu.kraenz.moshstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoshstoreApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(MoshstoreApplication.class, args);
  }
}
