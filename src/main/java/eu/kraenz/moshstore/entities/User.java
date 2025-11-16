package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(nullable = false, name = "name")
  private String name;

  @Column(nullable = false, name = "email")
  private String email;

  @Column(nullable = false, name = "password")
  private String password;

  @OneToMany(
      mappedBy = "user",
      cascade = {
        CascadeType.PERSIST,
        CascadeType.REMOVE,
      },
      orphanRemoval = true)
  @Builder.Default
  private List<Address> addresses = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "users_tags",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  @Builder.Default
  private Set<Tag> tags = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Profile profile;

  @ManyToMany(cascade = {CascadeType.REMOVE})
  @JoinTable(
      name = "wishlist",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  @Builder.Default
  private Set<Product> wishlist = new HashSet<>();

  public void addAddress(Address address) {
    addresses.add(address);
    address.setUser(this);
  }

  public void removeAddress(Address address) {
    addresses.remove(address);
    address.setUser(null);
  }

  public void addTag(String tag) {
    var theTag = new Tag(tag);
    tags.add(theTag);
    theTag.getUsers().add(this);
  }

  public void addProfile(Profile profile) {
    this.setProfile(profile);
    profile.setUser(this);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "("
        + "id = "
        + id
        + ", "
        + "name = "
        + name
        + ", "
        + "email = "
        + email
        + ")";
  }
}
