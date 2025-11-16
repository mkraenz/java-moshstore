package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {}
