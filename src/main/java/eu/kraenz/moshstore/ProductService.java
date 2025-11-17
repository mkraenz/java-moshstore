package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.repositories.CategoryRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

  @Transactional
  public void updatePrice() {
    productRepository.updatePriceByCategory(BigDecimal.valueOf(10), (byte) 1);
  }

  public void fetchProducts() {
    var product = new Product();
    product.setName("Black Tea");
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    var example = Example.of(product, matcher);
    var products = productRepository.findAll(example);
    products.forEach(System.out::println);
  }

  public void fetchProductsByCriteria() {
    var products =
        productRepository.findProductByCriteria(
            "Black Tea", BigDecimal.valueOf(1), BigDecimal.valueOf(15));
    products.forEach(System.out::println);
  }
}
