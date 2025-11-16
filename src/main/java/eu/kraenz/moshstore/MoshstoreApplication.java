package eu.kraenz.moshstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoshstoreApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(MoshstoreApplication.class, args);
    //    var users = ctx.getBean(UserRepository.class);
    //    var user = User.builder().name("Joe").email("hello@example.com").password("1234").build();
    //    users.save(user);
    //    users.findAll().forEach(u -> System.out.println(u.getEmail()));
    //    users.deleteById(1L);
    var userSvc = ctx.getBean(UserService.class);
    var productService = ctx.getBean(ProductService.class);
    //    productService.delete(1L);
    //    userSvc.wishlistAllProducts();
    //    productService.updatePrice();
    //    productService.fetchProducts();
    //    userSvc.fetchUser();
    userSvc.exerciseNineSeven();
  }
}
