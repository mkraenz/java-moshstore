package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {}
