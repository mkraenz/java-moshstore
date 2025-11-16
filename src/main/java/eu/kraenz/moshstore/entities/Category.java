package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Byte id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "category")
  @Builder.Default
  @ToString.Exclude
  private Set<Product> products = new HashSet<>();
}
