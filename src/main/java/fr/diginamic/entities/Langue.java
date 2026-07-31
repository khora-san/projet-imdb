package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente une langue d'un film
 */
@Entity
@Table(name = "LANGUE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Langue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

}