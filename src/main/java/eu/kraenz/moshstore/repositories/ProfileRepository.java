package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.Profile;
import eu.kraenz.moshstore.projections.UserSummary;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
  List<Profile> findByLoyaltyPointsBetween(int min, int max);
}
