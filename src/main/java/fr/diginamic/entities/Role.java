package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente un role via un personnage joué et un indicateur de casting principal,
 * ainsi que ses liens avec un film et une personne (acteurice)
 */
@Entity
@Table(name = "ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String personnage;

    private boolean principal;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;
}
