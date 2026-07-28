package fr.diginamic.dao;

import fr.diginamic.entities.Film;
import jakarta.persistence.EntityManager;

import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité Film
 */
public class FilmDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public FilmDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche un film par son id
     *
     * @param id l'id IMDB du film
     * @return un Optional contenant le film, ou vide si aucun ne correspond
     */
    public Optional<Film> findById(String id) {
        return Optional.ofNullable(em.find(Film.class, id));
    }

    /**
     * Sauvegarde dans la bdd un film
     *
     * @param film l'objet Film
     */
    public void save(Film film) {
        em.persist(film);
    }
}