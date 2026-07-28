package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "FILM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    @Column(nullable = false, length = 15)
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
}