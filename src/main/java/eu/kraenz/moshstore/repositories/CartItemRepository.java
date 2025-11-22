package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
