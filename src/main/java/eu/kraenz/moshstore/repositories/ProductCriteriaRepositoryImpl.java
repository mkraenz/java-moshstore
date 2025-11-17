package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {
  @PersistenceContext private final EntityManager entityManager;

  @Override
  public List<Product> findProductByCriteria(
      String name, BigDecimal minPrice, BigDecimal maxPrice) {
    var builder = entityManager.getCriteriaBuilder();
    var query = builder.createQuery(Product.class);
    // corresponds to: ... from products
    var root = query.from(Product.class);
    List<Predicate> predicates = new ArrayList<>();
    if (name != null) {
      // corresponds to: name LIKE %:name%
      predicates.add(builder.like(root.get("name"), "%" + name + "%"));
    }
    if (minPrice != null) {
      // corresponds to: price >= :minPrice
      predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
    }
    if (maxPrice != null) {
      // corresponds to: price <= :maxPrice
      predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
    }
    query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
    return entityManager.createQuery(query).getResultList();
  }
}
