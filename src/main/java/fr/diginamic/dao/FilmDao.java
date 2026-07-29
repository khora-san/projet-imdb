package fr.diginamic.dao;

import fr.diginamic.entities.Film;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
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

    /**
     * Recherche un film donné par son titre.
     *
     * @param titre le titre du Film
     * @return la liste des Films portant ce titre
     */
    public List<Film> findByTitre(String titre) {
        TypedQuery<Film> query = em.createQuery(
                "SELECT f FROM Film f WHERE LOWER(f.titre) = LOWER(:titre)", Film.class);
        query.setParameter("titre", titre);
        List<Film> filmList = query.getResultList();
        return filmList;
    }

    /**
     * Recherche les films sortis entre deux années données (bornes incluses).
     *
     * @param min l'année de début de la période (incluse)
     * @param max l'année de fin de la période (incluse)
     * @return la liste des Film dont l'année de sortie est comprise entre min et max
     */
    public List<Film> findBetween(Integer min, Integer max) {
        TypedQuery<Film> query = em.createQuery(
                "SELECT f FROM Film f WHERE f.anneeDebut BETWEEN :min AND :max", Film.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        List<Film> filmList = query.getResultList();
        return filmList;
    }
}