package fr.diginamic.dao;

import fr.diginamic.entities.LieuNaissance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité LieuNaissance
 */
public class LieuNaissanceDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public LieuNaissanceDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche un lieu de naissance par son libellé
     *
     * @param libelle le libellé du lieu de naissance
     * @return un Optional contenant le lieu de naissance trouvé, ou vide si aucun ne correspond
     */
    public Optional<LieuNaissance> findByNom(String libelle) {
        TypedQuery<LieuNaissance> query = em.createQuery(
                "SELECT l FROM LieuNaissance l WHERE l.libelle = :libelle", LieuNaissance.class);
        query.setParameter("libelle", libelle);
        List<LieuNaissance> lieuNaissanceList = query.getResultList();
        return lieuNaissanceList.isEmpty() ? Optional.empty() : Optional.of(lieuNaissanceList.get(0));
    }

    /**
     * Sauvegarde dans la bdd un lieu de naissance
     *
     * @param lieuNaissance l'objet LieuNaissance
     */
    public void save(LieuNaissance lieuNaissance) {
        em.persist(lieuNaissance);
    }
}
