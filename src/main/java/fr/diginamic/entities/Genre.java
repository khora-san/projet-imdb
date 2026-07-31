package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente un genre cinématographique
 */
@Entity
@Table(name = "GENRE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nom;

}