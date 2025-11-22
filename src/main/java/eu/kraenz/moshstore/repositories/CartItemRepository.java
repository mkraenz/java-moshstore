package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

// NOTE: Arguably, cart is the aggregate root and thus every save should go through the cart instead
// of saving individual cart items
public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
