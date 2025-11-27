package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {}
