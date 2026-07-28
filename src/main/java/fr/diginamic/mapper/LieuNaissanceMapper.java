package fr.diginamic.mapper;

import fr.diginamic.dao.LieuNaissanceDao;
import fr.diginamic.entities.LieuNaissance;

import java.util.Optional;

/**
 * Recherche un LieuNaissance existant à partir de son libellé,
 * ou le crée s'il n'existe pas encore
 */
public class LieuNaissanceMapper {

    private final LieuNaissanceDao lieuNaissanceDao;

    /**
     * @param lieuNaissanceDao le DAO utilisé pour rechercher et sauvegarder les lieux de naissance
     */
    public LieuNaissanceMapper(LieuNaissanceDao lieuNaissanceDao) {
        this.lieuNaissanceDao = lieuNaissanceDao;
    }

    /**
     * Recherche un lieu de naissance existant par son libellé, ou en crée un nouveau si aucun ne correspond.
     *
     * @param libelle le libellé du lieu de naissance
     * @return le LieuNaissance trouvé ou nouvellement créé
     */
    public LieuNaissance findOrCreate(String libelle) {

        Optional<LieuNaissance> lieuNaissanceExistant = lieuNaissanceDao.findByLibelle(libelle);
        if (lieuNaissanceExistant.isPresent()) {
            return lieuNaissanceExistant.get();
        } else {
            LieuNaissance nouveauLieuNaissance = new LieuNaissance();
            nouveauLieuNaissance.setLibelle(libelle);
            lieuNaissanceDao.save(nouveauLieuNaissance);
            return nouveauLieuNaissance;
        }

    }
}