package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente la structure brute d'un rôle telle qu'elle apparaît dans la source JSON,
 * avant toute transformation en entité Role (effectuée par RoleMapper).
 * Les champs correspondent directement aux clés du JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

    private String characterName;
    private PersonneDto acteur;
}