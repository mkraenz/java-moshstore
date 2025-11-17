package eu.kraenz.moshstore.repositories.specifications;

import eu.kraenz.moshstore.entities.Product;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {
  public static Specification<Product> hasName(String name) {
    return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
  }

  public static Specification<Product> hasPriceGte(BigDecimal min) {
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), min);
  }

  public static Specification<Product> hasPriceLte(BigDecimal max) {
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), max);
  }

  public static Specification<Product> hasCategory(String name) {
    return (root, query, cb) -> cb.equal(root.get("category").get("name"), name);
  }
}
