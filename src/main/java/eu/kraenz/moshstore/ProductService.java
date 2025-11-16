package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.repositories.CategoryRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class ProductService {
  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;

  @Transactional
  public void create() {
    Category category1 = categoryRepository.findById((byte) 1).orElseThrow();
    var product = Product.builder().name("Black Tea").price(new BigDecimal("0.75")).build();
    product.setCategory(category1);
    productRepository.save(product);
  }

  public void delete(long id) {
    productRepository.deleteById(id);
  }
}
