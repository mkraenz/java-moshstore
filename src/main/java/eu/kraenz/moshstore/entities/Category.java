package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Byte id;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "category")
  @ToString.Exclude
  private Set<Product> products = new HashSet();

  public Category(String name) {
    this.name = name;
  }

  public void addProduct(Product product) {
    this.products.add(product);
    product.setCategory(this);
  }
}
