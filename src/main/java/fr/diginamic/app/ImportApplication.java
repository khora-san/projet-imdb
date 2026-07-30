package fr.diginamic.app;

import fr.diginamic.dao.*;
import fr.diginamic.dto.FilmDto;
import fr.diginamic.mapper.*;
import fr.diginamic.services.ImportService;
import fr.diginamic.util.JpaUtil;

import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.util.List;

/**
 * Point d'entrée de l'application d'import : lit le fichier JSON source
 * et peuple la base de données avec les films et toutes leurs données associées.
 */
public class ImportApplication {

    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();

        // Mapper et DAO
        FilmMapper filmMapper = getFilmMapper(em);

        // Service
        ImportService importService = new ImportService(filmMapper, em);

        try {
            List<FilmDto> films = importService.parseFilms("/films.json");
            importService.persistFilms(films);
        } catch (IOException e) {
            System.err.println("Impossible de lire le fichier JSON source : " + e.getMessage());
        } finally {
            em.close();
            JpaUtil.close();
        }
    }

    /**
     * Construit un FilmMapper pleinement opérationnel, en instanciant et en reliant
     * entre eux tous les DAO et Mapper dont il dépend (directement ou indirectement).
     *
     * @param em l'EntityManager utilisé pour construire les DAO restants
     * @return un FilmMapper prêt à l'emploi, avec toute sa chaîne de dépendances construite
     */
    private static FilmMapper getFilmMapper(EntityManager em) {
        GenreDao genreDao = new GenreDao(em);
        LieuNaissanceDao lieuNaissanceDao = new LieuNaissanceDao(em);
        PersonneDao personneDao = new PersonneDao(em);
        FilmDao filmDao = new FilmDao(em);
        RoleDao roleDao = new RoleDao(em);
        PaysDao paysDao = new PaysDao(em);
        LangueDao langueDao = new LangueDao(em);

        // Mapper
        PaysMapper paysMapper = new PaysMapper(paysDao);
        LangueMapper langueMapper = new LangueMapper(langueDao);
        GenreMapper genreMapper = new GenreMapper(genreDao);
        LieuNaissanceMapper lieuNaissanceMapper = new LieuNaissanceMapper(lieuNaissanceDao);
        PersonneMapper personneMapper = new PersonneMapper(personneDao, lieuNaissanceMapper);
        RoleMapper roleMapper = new RoleMapper(roleDao);
        FilmMapper filmMapper = new FilmMapper(filmDao, paysMapper, langueMapper, genreMapper, personneMapper, roleMapper);
        return filmMapper;
    }
}