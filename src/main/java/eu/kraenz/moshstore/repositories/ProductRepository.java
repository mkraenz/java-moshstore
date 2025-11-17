package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Category;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.projections.ProductSummary;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository {
  List<Product> findByName(String name);

  List<Product> findByNameNotLike(String name);

  List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

  List<Product> findByPriceNullAndNameNull();

  List<Product> findByNameOrderByPrice(String name);

  List<Product> findTop5ByNameOrderByPrice(String name);

  @Query("select p from Product p where p.price between :minPrice and :maxPrice order by p.name")
  List<Product> findByPriceBetweenOrderByName(
      @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

  @Query("select count(p) from Product p where p.price between :minPrice and :maxPrice")
  long countByPrice(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

  @Modifying
  @Query("update Product p set p.price = :newPrice where p.category.id = :categoryId")
  void updatePriceByCategory(BigDecimal newPrice, Byte categoryId);

  @Query(
      "select new eu.kraenz.moshstore.projections.ProductSummaryTmp(p.id, p.name)"
          + " from Product p where p.category = :category")
  List<ProductSummary> findByCategory(@Param("category") Category category);
}
