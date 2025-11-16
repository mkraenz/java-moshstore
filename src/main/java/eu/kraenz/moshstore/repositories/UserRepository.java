package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.projections.UserSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
  @EntityGraph(attributePaths = {"tags", "addresses"})
  Optional<User> findByEmail(String email);

  @EntityGraph(attributePaths = "addresses")
  @Query("select u from User u")
  List<User> findAllWithAddresses();

  @Query(
      "select p.user.id as id, p.user.email as email from Profile p "
          + "where p.loyaltyPoints >= :points "
          + "order by p.user.email DESC")
  List<UserSummary> findByLoyaltyPointsGreaterThan(@Param("points") int points);
}
