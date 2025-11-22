package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "date_created", nullable = false, updatable = false)
  private LocalDate dateCreated = LocalDate.now();

  @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
  private Set<CartItem> items = new HashSet<>();
}
