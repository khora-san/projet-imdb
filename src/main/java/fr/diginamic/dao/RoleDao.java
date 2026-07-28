package fr.diginamic.dao;

import fr.diginamic.entities.Role;
import jakarta.persistence.EntityManager;

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
}
