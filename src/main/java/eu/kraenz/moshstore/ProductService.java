package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.repositories.CategoryRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import eu.kraenz.moshstore.repositories.specifications.ProductSpec;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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

  public void fetchProductsBySpecs(String name, BigDecimal minPrice, BigDecimal maxPrice) {
    Specification<Product> spec = Specification.unrestricted();
    if (name != null) spec = spec.and(ProductSpec.hasName(name));
    if (minPrice != null) spec = spec.and(ProductSpec.hasPriceGte(minPrice));
    if (maxPrice != null) spec = spec.and(ProductSpec.hasPriceLte(maxPrice));
    var products = productRepository.findAll(spec);
    products.forEach(System.out::println);
  }

  public void fetchSortedProducts() {
    // sort by name asc, then price desc
    var sort = Sort.by("name").and(Sort.by("price").descending());
    productRepository.findAll(sort).forEach(System.out::println);
  }

  public void fetchPaginatedProducts(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Product> currentPage = productRepository.findAll(pageRequest);
    List<Product> products = currentPage.getContent();
    products.forEach(System.out::println);
    System.out.println(currentPage.getTotalPages());
    System.out.println(currentPage.getTotalElements());
  }

  public void findByCategory() {
    //    List<Product> products = productRepository.findByCategoryName("bestverage");
    Specification<Product> spec = Specification.unrestricted();
    spec = spec.and(ProductSpec.hasCategory("bestverage")).and(ProductSpec.hasName("Black Tea"));
    List<Product> products = productRepository.findAll(spec);
    products.forEach(System.out::println);
  }
}
