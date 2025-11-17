package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
  List<Product> findProductByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);
}
