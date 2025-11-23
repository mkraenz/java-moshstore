package eu.kraenz.moshstore.controllers;

import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.repositories.CategoryRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import eu.kraenz.moshstore.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.StreamSupport;

// TODO remove this controller. only for dev

@AllArgsConstructor
@RestController
@RequestMapping("/dev")
class DevController {

  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/seed")
  @Transactional
  public String seedDatabase() {
    var yaml = new Yaml();
    InputStream inputStream =
        this.getClass()
            .getClassLoader()
            // this is relative to target/classes/ which is where maven puts files on build
            .getResourceAsStream("db/database.seed.yaml");
    Map<String, Object> seedData = yaml.load(inputStream);
    // certainly not the most elegant, but works for now
    var usersRaw = (ArrayList<Map<String, Object>>) seedData.get("users");
    var users =
        usersRaw.stream()
            .map(
                user ->
                    User.builder()
                        .email((String) user.get("email"))
                        .name((String) user.get("name"))
                        .password(passwordEncoder.encode(String.valueOf(Math.random())))
                        .build());
    userRepository.saveAll(users.toList());

    var categoriesRaw = (ArrayList<Map<String, Object>>) seedData.get("categories");
    var categories = categoriesRaw.stream().map(c -> new Category((String) c.get("name"))).toList();
    var savedCategories = categoryRepository.saveAll(categories);
    System.out.println(savedCategories);

    var productsRaw = (ArrayList<Map<String, Object>>) seedData.get("products");
    var products =
        productsRaw.stream()
            .map(
                p -> {
                  var categoryName = (String) p.get("category");
                  //                  var test =
                  //                      StreamSupport.stream(savedCategories.spliterator(), false)
                  //                          .filter(c -> c.getName().equals(categoryName))
                  //                          .toList();
                  var category =
                      StreamSupport.stream(savedCategories.spliterator(), false)
                          .filter(c -> c.getName().equals(categoryName))
                          .findFirst()
                          .orElseThrow();
                  return Product.builder()
                      .name((String) p.get("name"))
                      .description((String) p.get("description"))
                      .price(BigDecimal.valueOf((double) p.get("price")))
                      .category(category)
                      .build();
                })
            .toList();
    productRepository.saveAll(products);
    return "success";
  }
}
