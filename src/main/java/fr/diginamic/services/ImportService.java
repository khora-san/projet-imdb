package fr.diginamic.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.dto.FilmDto;
import fr.diginamic.mapper.FilmMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Orchestre l'import des films depuis une source JSON vers la base de données,
 * en s'appuyant sur FilmMapper pour résoudre et persister chaque film et ses données associées.
 */
public class ImportService {

    private final FilmMapper filmMapper;

    /**
     * @param filmMapper le mapper utilisé pour résoudre et sauvegarder chaque film importé
     */
    public ImportService(FilmMapper filmMapper) {
        this.filmMapper = filmMapper;
    }

    /**
     * Lit et désérialise le fichier JSON source en une liste de FilmDto.
     *
     * @param cheminFichier le chemin (ressource classpath) du fichier JSON à lire
     * @return la liste des FilmDto lus depuis le fichier
     * @throws IOException si le fichier est introuvable ou si le JSON est mal formé
     */
    public List<FilmDto> parseFilms(String cheminFichier) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream(cheminFichier);
        return objectMapper.readValue(inputStream, new TypeReference<List<FilmDto>>() {});
    }

    /**
     * Persiste chaque film de la liste donnée en base de données via FilmMapper.
     *
     * @param films la liste des FilmDto à importer
     */
    public void persistFilms(List<FilmDto> films) {
        for (FilmDto filmDto : films) {
            filmMapper.findOrCreate(filmDto);
        }
        System.out.println("Import terminé : " + films.size() + " films traités.");
    }
}