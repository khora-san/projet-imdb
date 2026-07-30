package fr.diginamic.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.dto.FilmDto;
import fr.diginamic.mapper.FilmMapper;
import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Orchestre l'import des films depuis une source JSON vers la base de données,
 * en s'appuyant sur FilmMapper pour résoudre et persister chaque film et ses données associées.
 */
public class ImportService {

    private final FilmMapper filmMapper;
    private final EntityManager em;

    /**
     * @param filmMapper le mapper utilisé pour résoudre et sauvegarder chaque film importé
     */
    public ImportService(FilmMapper filmMapper, EntityManager em) {
        this.filmMapper = filmMapper;
        this.em = em;
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
        return objectMapper.readValue(inputStream, new TypeReference<List<FilmDto>>() {
        });
    }

    /**
     * Persiste chaque film de la liste donnée en base de données via FilmMapper,
     * dans une transaction dédiée par film : en cas d'erreur sur un film,
     * la transaction correspondante est annulée (rollback) et l'import continue
     * avec les films suivants, sans perdre les films déjà importés avec succès.
     *
     * @param films la liste des FilmDto à importer
     */
    public void persistFilms(List<FilmDto> films) {
        int compteurFilmsTraites = 0;
        int compteurNouveaux = 0;
        Set<String> idsTraites = new HashSet<>();
        long debut = System.currentTimeMillis();

        for (FilmDto filmDto : films) {
            try {
                em.getTransaction().begin();
                filmMapper.findOrCreate(filmDto);
                em.getTransaction().commit();
                compteurFilmsTraites++;
                if (idsTraites.add(filmDto.getId())) {
                    compteurNouveaux++;
                }
            } catch (Exception e) {
                em.getTransaction().rollback();
                System.err.println("Erreur : " + e.getMessage());
                System.err.println("Film concerné : " + filmDto.getId() + " " + filmDto.getNom());
            }
        }
        long dureeSecondes = (System.currentTimeMillis() - debut) / 1000;
        System.out.println("Import terminé : " + compteurFilmsTraites + "/" + films.size() + " films traités en " + dureeSecondes + " secondes.");
        System.out.println(compteurNouveaux + " films ajoutés en base");
    }
}