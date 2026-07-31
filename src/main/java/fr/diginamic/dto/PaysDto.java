package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente la structure brute des informations de pays telles qu'elles apparaissent dans la source JSON,
 * avant toute transformation en entité Pays (effectuée par PaysMapper).
 * Les champs correspondent directement aux clés du JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PaysDto {

    private String nom;
}