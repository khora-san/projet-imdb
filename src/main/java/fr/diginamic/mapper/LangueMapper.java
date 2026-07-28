package fr.diginamic.mapper;

import fr.diginamic.dao.LangueDao;
import fr.diginamic.entities.Langue;

import java.util.Optional;

/**
 * Recherche une Langue existant à partir de son nom,
 * ou la crée si elle n'existe pas encore
 */
public class LangueMapper {

    private final LangueDao langueDao;

    /**
     * @param langueDao le DAO utilisé pour rechercher et sauvegarder les langues
     */
    public LangueMapper(LangueDao langueDao) {
        this.langueDao = langueDao;
    }

    /**
     * Recherche une langue existante par son nom, ou en crée unw nouvelle si aucune ne correspond.
     *
     * @param nom le nom d'une langue
     * @return la Langue trouvée ou nouvellement créée
     */
    public Langue findOrCreate(String nom) {

        Optional<Langue> langueExistant = langueDao.findByNom(nom);
        if (langueExistant.isPresent()) {
            return langueExistant.get();
        } else {
            Langue nouvelleLangue = new Langue();
            nouvelleLangue.setNom(nom);
            langueDao.save(nouvelleLangue);
            return nouvelleLangue;
        }

    }
}