package fr.diginamic.mapper;

import fr.diginamic.dao.GenreDao;
import fr.diginamic.entities.Genre;

import java.util.Optional;

/**
 * Recherche un Genre existant à partir de son nom,
 * ou le crée s'il n'existe pas encore
 */
public class GenreMapper {

    private final GenreDao genreDao;

    /**
     * @param genreDao le DAO utilisé pour rechercher et sauvegarder les genres
     */
    public GenreMapper(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * Recherche un genre existant par son nom, ou en crée un nouveau si aucun ne correspond.
     *
     * @param nom le nom du genre
     * @return le Genre trouvé ou nouvellement créé
     */
    public Genre findOrCreate(String nom) {

        Optional<Genre> genreExistant = genreDao.findByNom(nom);
        if (genreExistant.isPresent()) {
            return genreExistant.get();
        } else {
            Genre nouveauGenre = new Genre();
            nouveauGenre.setNom(nom);
            genreDao.save(nouveauGenre);
            return nouveauGenre;
        }

    }
}