package fr.diginamic.dao;

import fr.diginamic.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Expose les méthodes métier de l'entité Genre
 */
public class GenreDao {
    private final EntityManager em;

    /**
     *
     * @param em l'Entity Manager utilisé pour
     *           les opérations de persistance
     *
     */
    public GenreDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Recherche un genre par son nom
     *
     * @param nom le nom du genre
     * @return un Optional contenant le genre trouvé, ou vide si aucun ne correspond
     */
    public Optional<Genre> findByNom(String nom) {
        TypedQuery<Genre> query = em.createQuery(
                "SELECT g FROM Genre g WHERE g.nom = :nom", Genre.class);
        query.setParameter("nom", nom);
        List<Genre> genreList = query.getResultList();
        return genreList.isEmpty() ? Optional.empty() : Optional.of(genreList.get(0));
    }

    /**
     * Sauvegarde dans la bdd un genre
     *
     * @param genre l'objet Genre
     */
    public void save(Genre genre) {
        em.persist(genre);
    }
}
