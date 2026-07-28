package fr.diginamic.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LIEU_NAISSANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LieuNaissance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String libelle;

}