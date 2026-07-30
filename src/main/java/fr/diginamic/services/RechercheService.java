package fr.diginamic.services;

import fr.diginamic.dao.FilmDao;
import fr.diginamic.dao.PersonneDao;
import fr.diginamic.dao.RoleDao;
import fr.diginamic.entities.Film;
import fr.diginamic.entities.Personne;
import fr.diginamic.entities.Role;

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
    public List<Film> rechercherFilmsParTitre(String titre) {
        return filmDao.findByTitre(titre);
    }

    /**
     * Recherche tous les rôles d'un acteur donné, permettant de reconstituer sa filmographie.
     *
     * @param personne l'acteur dont on cherche la filmographie
     * @return la liste des Role associés à cet acteur
     */
    public List<Role> rechercherFilmographie(Personne personne) {
        return roleDao.findByPersonne(personne);
    }

    /**
     * Recherche le casting d'un film donné
     *
     * @param film le film dont on cherche le casting
     * @return la liste des Role associés à ce film
     */
    public List<Role> rechercherCasting(Film film) {
        return roleDao.findByFilm(film);
    }

    /**
     * Recherche les films sortis entre deux années données (bornes incluses).
     *
     * @param min l'année de début de la période (incluse)
     * @param max l'année de fin de la période (incluse)
     * @return la liste des Films dont l'année de sortie est comprise entre min et max
     */
    public List<Film> rechercherFilmsEntreAnnees(Integer min, Integer max) {
        return filmDao.findBetween(min, max);
    }

    /**
     * Recherche les films dans lesquels deux acteurs donnés ont chacun un rôle.
     *
     * @param acteur1 le premier acteur
     * @param acteur2 le second acteur
     * @return la liste des Films communs aux deux acteurs
     */
    public List<Film> rechercherFilmsCommuns(Personne acteur1, Personne acteur2) {
        return roleDao.findFilmsCommunsEntreActeurs(acteur1, acteur2);
    }

    /**
     * Recherche les acteurs ayant chacun un rôle dans deux films donnés.
     *
     * @param film1 le premier Film
     * @param film2 le second Film
     * @return la liste des Personnes communes aux deux films
     */
    public List<Personne> rechercherActeursCommuns(Film film1, Film film2) {
        return roleDao.findActeursByDeuxFilmsCommuns(film1, film2);
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
    public List<Film> rechercherFilmsEntreAnneesAvecActeur(Personne personne, Integer min, Integer max) {
        return roleDao.findFilmsByPersonneAndAnnees(personne, min, max);
    }
}
