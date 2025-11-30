package eu.kraenz.moshstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;

@SpringBootApplication
public class MoshstoreApplication {

  public static void main(String[] args) {

    TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
    ConfigurableApplicationContext ctx = SpringApplication.run(MoshstoreApplication.class, args);
  }
}
