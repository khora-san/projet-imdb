package fr.diginamic.services;

import fr.diginamic.dao.FilmDao;
import fr.diginamic.dao.PersonneDao;
import fr.diginamic.dao.RoleDao;
import fr.diginamic.entities.Film;
import fr.diginamic.entities.Personne;

import java.util.List;

/**
 * Interroge la base selon des requêtes prédéfinies
 */
public class RechercheService {

    private final RoleDao roleDao;
    private final FilmDao filmDao;
    private final PersonneDao personneDao;

    /**
     * @param roleDao     le DAO utilisé pour rechercher des rôles
     * @param filmDao     le DAO utilisé pour rechercher des films
     * @param personneDao le DAO utilisé pour rechercher des personnes
     */
    public RechercheService(RoleDao roleDao, FilmDao filmDao, PersonneDao personneDao) {
        this.roleDao = roleDao;
        this.filmDao = filmDao;
        this.personneDao = personneDao;
    }

    /**
     * Recherche les personnes correspondant à un nom donné.
     *
     * @param nom le nom recherché (comparaison exacte, insensible à la casse)
     * @return la liste des Personnes dont l'identité correspond à ce nom
     */
    public List<Personne> rechercherActeursParNom(String nom) {
        return personneDao.findByIdentite(nom);
    }

    /**
     * Recherche les films correspondant à un titre donné.
     *
     * @param titre le titre recherché (comparaison exacte, insensible à la casse)
     * @return la liste des Films dont le titre correspond
     */
    public List<Film> rechercherFilmsParTitre(String titre){
        return filmDao.findByTitre(titre);
    }
}
