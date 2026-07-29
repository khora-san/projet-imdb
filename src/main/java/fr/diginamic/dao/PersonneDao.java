package fr.diginamic.dao;

import fr.diginamic.entities.Personne;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité Personne
 */
public class PersonneDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public PersonneDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche une personne par son id
     *
     * @param id l'id IMDB de la personne
     * @return un Optional contenant la personne, ou vide si aucune ne correspond
     */
    public Optional<Personne> findById(String id) {
        return Optional.ofNullable(em.find(Personne.class, id));
    }

    /**
     * Sauvegarde dans la bdd une personne
     *
     * @param personne l'objet Personne
     */
    public void save(Personne personne) {
        em.persist(personne);
    }

    /**
     * Recherche une personne donnée par son identité.
     *
     * @param identite l'identité (Prénom Nom) de la personne
     * @return la liste des Personnes portant cette identité
     */
    public List<Personne> findByIdentite(String identite) {
        TypedQuery<Personne> query = em.createQuery(
                "SELECT p FROM Personne p WHERE LOWER(p.identite) = LOWER(:identite)", Personne.class);
        query.setParameter("identite", identite);
        List<Personne> personneList = query.getResultList();
        return personneList;
    }


}