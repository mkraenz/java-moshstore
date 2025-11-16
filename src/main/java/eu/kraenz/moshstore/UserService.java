package eu.kraenz.moshstore;

import eu.kraenz.moshstore.entities.Address;
import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.repositories.AddressRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import eu.kraenz.moshstore.repositories.ProfileRepository;
import eu.kraenz.moshstore.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final EntityManager entityManager;
  private final ProfileRepository profileRepository;
  private final AddressRepository addressRepository;
  private final ProductRepository productRepository;

  @Transactional
  public void showEntityStates() {
    var user = User.builder().name("Joe").email("hello@example.com").password("1234").build();
    logEntityState(user);
    userRepository.save(user);
    logEntityState(user);
  }

  private void logEntityState(User user) {
    if (entityManager.contains(user)) {
      System.out.println("Persistent state");
    } else {
      System.out.println("Transient or Detached state");
    }
  }

  @Transactional
  public void showRelatedEntities() {
    //    Profile profile = profileRepository.findById(2L).orElseThrow();
    Address address = addressRepository.findById(1L).orElseThrow();
    System.out.println(address.getCity());
  }

  public void persistRelated() {
    var user = User.builder().name("Joe").email("hello@example.com").password("1234").build();
    var address = Address.builder().city("london").state("NY").zip("12345").street("baker").build();
    user.addAddress(address);
    userRepository.save(user);
  }

  @Transactional
  public void deleteRelated() {
    User user = userRepository.findById(5L).orElseThrow();
    var address = user.getAddresses().getFirst();
    user.removeAddress(address);
    userRepository.save(user);
  }

  @Transactional
  public void wishlistAllProducts() {
    User user = userRepository.findById(5L).orElseThrow();
    productRepository.findAll().forEach(user.getWishlist()::add);
    userRepository.save(user);
  }
}
