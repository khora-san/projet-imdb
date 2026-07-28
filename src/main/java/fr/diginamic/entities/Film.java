package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un film et ses métadonnées (titre, année de sortie, note, résumé),
 * ainsi que ses relations avec la langue, le pays, les genres, et les réalisateurs.
 */
@Entity
@Table(name = "FILM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    @Column(nullable = false, length = 15)
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(nullable = false, length = 500)
    private String titre;

    @Column(name = "annee_debut", nullable = false)
    private Integer anneeDebut;

    @Column(precision = 3, scale = 1)
    private BigDecimal note;

    @Lob
    private String resume;

    @ManyToOne
    @JoinColumn(name = "langue_id")
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "pays_id", nullable = false)
    private Pays pays;

    @ManyToMany
    @JoinTable(
            name = "FILM_GENRE",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "PERSONNE_FILM",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "personne_id")
    )
    private Set<Personne> realisateurs = new HashSet<>();
}