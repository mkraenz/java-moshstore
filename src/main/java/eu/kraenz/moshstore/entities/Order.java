package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders", schema = "moshstore")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private PaymentStatus status = PaymentStatus.PENDING;

  @ColumnDefault("CURRENT_TIMESTAMP")
  // insertable=false, updateable=false to get rid of nullability error
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @NotNull
  @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "customer_id")
  private User customer;

  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Builder.Default
  private Set<OrderItem> items = new LinkedHashSet<>();

  public static Order fromCart(Cart cart, User user) {
    var order = Order.builder().customer(user).totalPrice(cart.getTotalPrice()).build();
    cart.getItems().forEach(i -> order.addItemFromCart(i));
    return order;
  }

  private void addItemFromCart(CartItem cartItem) {
    var orderItem =
        OrderItem.builder()
            .order(this)
            .product(cartItem.getProduct())
            .quantity(cartItem.getQuantity())
            .unitPrice(cartItem.getProduct().getPrice())
            .totalPrice(cartItem.getTotalPrice())
            .build();
    items.add(orderItem);
  }
}
