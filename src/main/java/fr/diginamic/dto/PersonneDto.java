package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente la structure brute d'une personne telle qu'elle apparaît dans la source JSON,
 * avant toute transformation en entité Personne (effectuée par PersonneMapper).
 * Les champs correspondent directement aux clés du JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PersonneDto {

    private String id;
    private String identite;
    private NaissanceDto naissance;
}