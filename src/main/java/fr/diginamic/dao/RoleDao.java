package fr.diginamic.dao;

import fr.diginamic.entities.Film;
import fr.diginamic.entities.Personne;
import fr.diginamic.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Expose les méthodes métier de l'entité Role
 */
public class RoleDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public RoleDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Sauvegarde dans la bdd un role
     *
     * @param role l'objet Role
     */
    public void save(Role role) {
        em.persist(role);
    }

    /**
     * Recherche les rôles associés à un film donné.
     *
     * @param film le Film dont on veut récupérer le casting
     * @return la liste des Role associés à ce film
     */
    public List<Role> findByFilm(Film film) {
        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.film = :film", Role.class);
        query.setParameter("film", film);
        List<Role> roleList = query.getResultList();
        return roleList;
    }

    /**
     * Recherche les rôles associés à un acteur donné.
     *
     * @param personne la Personne dont on veut récupérer les roles
     * @return la liste des Role associés à cette personne
     */
    public List<Role> findByPersonne(Personne personne) {
        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.personne = :personne", Role.class);
        query.setParameter("personne", personne);
        List<Role> roleList = query.getResultList();
        return roleList;
    }

    /**
     * Recherche les films d'un acteur donné
     * et sortis entre deux années données (bornes incluses).
     *
     * @param personne la Personne dont on veut filtrer les films
     * @param min      l'année de début de la période (incluse)
     * @param max      l'année de fin de la période (incluse)
     * @return la liste des Film correspondante
     */
    public List<Film> findFilmsByPersonneAndAnnees(Personne personne, Integer min, Integer max) {
        TypedQuery<Film> query = em.createQuery(
                "SELECT DISTINCT r.film FROM Role r WHERE r.personne = :personne AND r.film.anneeDebut BETWEEN :min AND :max", Film.class);
        query.setParameter("personne", personne);
        query.setParameter("min", min);
        query.setParameter("max", max);
        List<Film> filmList = query.getResultList();
        return filmList;
    }

    /**
     * Recherche les films dans lesquels deux acteurs donnés ont chacun un rôle.
     *
     * @param personne1 le premier acteur
     * @param personne2 le second acteur
     * @return la liste des Films communs aux deux acteurs
     */
    public List<Film> findFilmsCommunsEntreActeurs(Personne personne1, Personne personne2) {
        TypedQuery<Film> query = em.createQuery(
                "SELECT DISTINCT r1.film FROM Role r1, Role r2 " +
                        "WHERE r1.film = r2.film AND r1.personne = :acteur1 AND r2.personne = :acteur2", Film.class);
        query.setParameter("acteur1", personne1);
        query.setParameter("acteur2", personne2);
        List<Film> filmList = query.getResultList();
        return filmList;
    }

    /**
     * Recherche les acteurs ayant chacun un rôle dans deux films donnés.
     *
     * @param film1 le premier Film
     * @param film2 le second Film
     * @return la liste des Personnes communes aux deux films
     */
    public List<Personne> findActeursByDeuxFilmsCommuns(Film film1, Film film2) {
        TypedQuery<Personne> query = em.createQuery(
                "SELECT DISTINCT r1.personne FROM Role r1, Role r2 " +
                        "WHERE r1.personne = r2.personne AND r1.film = :film1 AND r2.film = :film2", Personne.class);
        query.setParameter("film1", film1);
        query.setParameter("film2", film2);
        List<Personne> personneList = query.getResultList();
        return personneList;
    }

}

