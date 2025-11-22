package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "quantity")
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public BigDecimal getTotalPrice() {
    return this.getProduct().getPrice().multiply(BigDecimal.valueOf(this.getQuantity()));
  }
}
