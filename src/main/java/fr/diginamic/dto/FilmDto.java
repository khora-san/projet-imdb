package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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