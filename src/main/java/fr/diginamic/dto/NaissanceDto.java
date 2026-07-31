package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente la structure brute des informations de naissance telles qu'elles apparaissent dans la source JSON,
 * avant toute transformation en entité LieuNaissance et en attribut dateNaissance de Personne
 * (effectuée par LieuNaissanceMapper et PersonneMapper respectivement).
 * Les champs correspondent directement aux clés du JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class NaissanceDto {

    private String dateNaissance;
    private String lieuNaissance;

}