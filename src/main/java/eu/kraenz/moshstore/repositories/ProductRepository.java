package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Product;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @EntityGraph(attributePaths = "category")
  List<Product> findByCategoryId(Byte categoryId);
}
