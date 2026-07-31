package fr.diginamic.mapper;

import fr.diginamic.dao.LangueDao;
import fr.diginamic.entities.Langue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Recherche une Langue existant à partir de son nom,
 * ou la crée si elle n'existe pas encore
 */
public class LangueMapper {

    private final LangueDao langueDao;
    private final Map<String, Langue> cache = new HashMap<>();

    /**
     * @param langueDao le DAO utilisé pour rechercher et sauvegarder les langues
     */
    public LangueMapper(LangueDao langueDao) {
        this.langueDao = langueDao;
    }

    /**
     * Recherche une langue existante par son nom, ou en crée une nouvelle si aucune ne correspond.
     *
     * @param nom le nom d'une langue
     * @return la Langue trouvée ou nouvellement créée
     */
    public Langue findOrCreate(String nom) {
        String cle = nom.toLowerCase();
        Langue langue = cache.get(cle);
        if (langue != null) {
            return langue;
        }
        Optional<Langue> langueExistant = langueDao.findByNom(nom);
        if (langueExistant.isPresent()) {
            langue = langueExistant.get();
        } else {
            langue = new Langue();
            langue.setNom(nom);
            langueDao.save(langue);
        }
        cache.put(cle, langue);
        return langue;
    }
}