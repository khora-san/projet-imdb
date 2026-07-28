package fr.diginamic.mapper;

import fr.diginamic.dao.PaysDao;
import fr.diginamic.entities.Pays;

import java.util.Optional;

/**
 * Recherche un Pays existant à partir de son nom,
 * ou le crée s'il n'existe pas encore
 */
public class PaysMapper {

    private final PaysDao paysDao;

    /**
     * @param paysDao le DAO utilisé pour rechercher et sauvegarder les pays
     */
    public PaysMapper(PaysDao paysDao) {
        this.paysDao = paysDao;
    }

    /**
     * Recherche un pays existant par son nom, ou en crée un nouveau si aucun ne correspond.
     *
     * @param nom le nom du pays
     * @return le Pays trouvé ou nouvellement créé
     */
    public Pays findOrCreate(String nom) {

        Optional<Pays> paysExistant = paysDao.findByNom(nom);
        if (paysExistant.isPresent()) {
            return paysExistant.get();
        } else {
            Pays nouveauPays = new Pays();
            nouveauPays.setNom(nom);
            paysDao.save(nouveauPays);
            return nouveauPays;
        }

    }
}