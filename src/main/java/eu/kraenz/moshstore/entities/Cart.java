package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

  @OneToMany(
      mappedBy = "cart",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<CartItem> items = new ArrayList<>();

  public BigDecimal getTotalPrice() {
    return this.getItems().stream()
        .map(CartItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Optional<CartItem> findItem(Long productId) {
    return this.getItems().stream().filter(i -> i.getProduct().getId() == productId).findFirst();
  }

  public CartItem findItemOrNull(Long productId) {
    return findItem(productId).orElse(null);
  }

  public void clear() {
    getItems().clear();
  }
}
