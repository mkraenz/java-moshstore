package eu.kraenz.moshstore.repositories;

import eu.kraenz.moshstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
