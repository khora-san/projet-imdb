package fr.diginamic.dao;

import fr.diginamic.entities.Pays;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité Pays
 */
public class PaysDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public PaysDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche un pays par son nom
     *
     * @param nom le nom du pays
     * @return un Optional contenant le pays trouvé, ou vide si aucun ne correspond
     */
    public Optional<Pays> findByNom(String nom) {
        TypedQuery<Pays> query = em.createQuery(
                "SELECT p FROM Pays p WHERE p.nom = :nom", Pays.class);
        query.setParameter("nom", nom);
        List<Pays> paysList = query.getResultList();
        return paysList.isEmpty() ? Optional.empty() : Optional.of(paysList.get(0));
    }

    /**
     * Sauvegarde dans la bdd un pays
     *
     * @param pays l'objet Pays
     */
    public void save(Pays pays) {
        em.persist(pays);
    }
}
