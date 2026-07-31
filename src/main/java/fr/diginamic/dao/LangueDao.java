package fr.diginamic.dao;


import fr.diginamic.entities.Langue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité Langue
 */
public class LangueDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public LangueDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche une langue par son nom
     *
     * @param nom le nom de la langue
     * @return un Optional contenant la langue trouvée, ou vide si aucune ne correspond
     */
    public Optional<Langue> findByNom(String nom) {
        TypedQuery<Langue> query = em.createQuery(
                "SELECT l FROM Langue l WHERE LOWER(l.nom) = LOWER(:nom)", Langue.class);
        query.setParameter("nom", nom);
        List<Langue> langueList = query.getResultList();
        return langueList.isEmpty() ? Optional.empty() : Optional.of(langueList.get(0));
    }

    /**
     * Sauvegarde dans la bdd une langue
     *
     * @param langue l'objet Langue
     */
    public void save(Langue langue) {
        em.persist(langue);
    }
}
