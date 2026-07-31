package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Représente la structure brute d'un film telle qu'elle apparaît dans la source JSON,
 * avant toute transformation en entité Film (effectuée par FilmMapper). Les champs
 * correspondent directement aux clés du JSON — certains restent volontairement en
 * String (comme rating ou anneeSortie) car leur parsing vers un type précis
 * (BigDecimal, Integer...) est délégué au Mapper, pas géré ici.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class FilmDto {

    private String id;
    private PaysDto pays;
    private String nom;
    private String rating;
    private String plot;
    private String langue;
    private List<PersonneDto> realisateurs;
    private List<PersonneDto> castingPrincipal;
    private String anneeSortie;
    private List<RoleDto> roles;
    private List<String> genres;

}