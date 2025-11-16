package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "street", nullable = false)
  private String street;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "zip", nullable = false)
  private String zip;

  @Column(name = "state", nullable = true)
  private String state;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;
}
