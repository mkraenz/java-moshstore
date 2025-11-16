package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {}
