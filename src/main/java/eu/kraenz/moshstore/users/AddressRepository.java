package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {}
