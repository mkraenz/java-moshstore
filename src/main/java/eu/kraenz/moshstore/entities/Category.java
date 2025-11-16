package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @Column(name = "id")
  private Byte id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "category")
  private Set<Product> products = new HashSet<>();
}
