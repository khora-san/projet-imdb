package fr.diginamic.mapper;

import fr.diginamic.dao.PersonneDao;
import fr.diginamic.dto.PersonneDto;
import fr.diginamic.entities.LieuNaissance;
import fr.diginamic.entities.Personne;
import fr.diginamic.util.ParsingUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

/**
 * Recherche une Personne existante à partir de son id IMDB,
 * ou la crée à partir d'un PersonneDto si elle n'existe pas encore.
 */
public class PersonneMapper {

    private final PersonneDao personneDao;
    private final LieuNaissanceMapper lieuNaissanceMapper;

    /**
     * @param personneDao         le DAO utilisé pour rechercher et sauvegarder les personnes
     * @param lieuNaissanceMapper le mapper utilisé pour résoudre le lieu de naissance associé
     */
    public PersonneMapper(PersonneDao personneDao, LieuNaissanceMapper lieuNaissanceMapper) {
        this.personneDao = personneDao;
        this.lieuNaissanceMapper = lieuNaissanceMapper;
    }

    /**
     * Recherche une personne existante par son id, ou en crée une nouvelle à partir du DTO si aucune ne correspond.
     *
     * @param dto le DTO contenant les données brutes de la personne
     * @return la Personne trouvée ou nouvellement créée
     */
    public Personne findOrCreate(PersonneDto dto) {

        Optional<Personne> personneExistante = personneDao.findById(dto.getId());
        if (personneExistante.isPresent()) {
            return personneExistante.get();
        }

        LocalDate dateNaissance = parseDateNaissance(dto.getNaissance().getDateNaissance());

        String libelle = ParsingUtils.nullIfBlank(dto.getNaissance().getLieuNaissance());
        LieuNaissance lieuNaissance = (libelle == null) ? null : lieuNaissanceMapper.findOrCreate(libelle);

        Personne nouvellePersonne = new Personne(dto.getId(), dto.getIdentite(), dateNaissance, lieuNaissance);
        personneDao.save(nouvellePersonne);
        return nouvellePersonne;
    }

    /**
     * Parse une date de naissance brute (format "Month d yyyy") en LocalDate.
     * Retourne null si la chaîne est vide ou si le format ne peut pas être interprété
     * (année manquante, valeur invalide, etc.).
     *
     * @param raw la date de naissance brute telle qu'elle apparaît dans le JSON
     * @return la LocalDate correspondante, ou null si non interprétable
     */
    private LocalDate parseDateNaissance(String raw) {
        raw = ParsingUtils.nullIfBlank(raw);
        if (raw == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
        try {
            return LocalDate.parse(raw, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
