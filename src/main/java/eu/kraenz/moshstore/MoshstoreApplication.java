package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.entities.Profile;
import eu.kraenz.moshstore.entities.User;
import java.math.BigDecimal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoshstoreApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(MoshstoreApplication.class, args);
    var user =
        User.builder().name("Peter").email("test@example.com").password("pass").id(1L).build();
    var profile = Profile.builder().bio("bio").build();

    user.addProfile(profile);

    //    TODO add category and product
    var category = new Category("bikes");
    var product =
        Product.builder().price(new BigDecimal("794.99")).name("Mountain Bike Kira 21f").build();
    category.addProduct(product);

    //    user.addTag("blue");
    System.out.println(product);
  }
}
