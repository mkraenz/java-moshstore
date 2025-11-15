package eu.kraenz.moshstore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "profiles")
public class Profile {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "bio")
  private String bio;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "loyalty_points", nullable = false)
  private Integer loyaltyPoints;

  @OneToOne
  // profile and user have identical IDs
  @JoinColumn(name = "id")
  @MapsId
  @ToString.Exclude
  private User user;
}
