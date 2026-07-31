package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente un pays associé à un film
 */
@Entity
@Table(name = "PAYS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

}