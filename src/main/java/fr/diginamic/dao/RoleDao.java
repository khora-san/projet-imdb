package fr.diginamic.dao;

import fr.diginamic.entities.Film;
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
}

