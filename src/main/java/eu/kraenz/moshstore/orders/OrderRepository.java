package eu.kraenz.moshstore.orders;

import eu.kraenz.moshstore.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
  List<Order> findByCustomerId(Long customerId);

  Optional<Order> findByIdAndCustomerId(Long id, Long customerId);
}
