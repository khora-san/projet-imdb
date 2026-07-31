package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Représente une personne et ses données (identité, date de naissance),
 * ainsi que ses relations avec un lieu de naissance
 */
@Entity
@Table(name = "PERSONNE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personne {

    @Id
    @Column(nullable = false, length = 15)
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(nullable = false, length = 255)
    private String identite;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @ManyToOne
    @JoinColumn(name = "lieu_naissance_id")
    private LieuNaissance lieuNaissance;
}