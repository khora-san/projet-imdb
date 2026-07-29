package fr.diginamic.mapper;

import fr.diginamic.dao.FilmDao;
import fr.diginamic.dto.FilmDto;
import fr.diginamic.dto.PersonneDto;
import fr.diginamic.dto.RoleDto;
import fr.diginamic.entities.*;
import fr.diginamic.util.ParsingUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Recherche un Film existant à partir de son id IMDB, ou le crée à partir d'un FilmDto
 * si aucun ne correspond, en résolvant au passage son pays, sa langue, ses genres,
 * ses réalisateurs et ses rôles (casting).
 */
public class FilmMapper {
    private final FilmDao filmDao;
    private final PaysMapper paysMapper;
    private final LangueMapper langueMapper;
    private final GenreMapper genreMapper;
    private final PersonneMapper personneMapper;
    private final RoleMapper roleMapper;

    /**
     * @param filmDao        le DAO utilisé pour rechercher et sauvegarder les films
     * @param paysMapper     le mapper utilisé pour résoudre le pays associé
     * @param langueMapper   le mapper utilisé pour résoudre la langue associée
     * @param genreMapper    le mapper utilisé pour résoudre les genres associés
     * @param personneMapper le mapper utilisé pour résoudre les réalisateurs et les acteurs
     * @param roleMapper     le mapper utilisé pour construire et sauvegarder les rôles associés
     */
    public FilmMapper(FilmDao filmDao, PaysMapper paysMapper, LangueMapper langueMapper, GenreMapper genreMapper, PersonneMapper personneMapper, RoleMapper roleMapper) {
        this.filmDao = filmDao;
        this.paysMapper = paysMapper;
        this.langueMapper = langueMapper;
        this.genreMapper = genreMapper;
        this.personneMapper = personneMapper;
        this.roleMapper = roleMapper;
    }

    /**
     * Recherche un film existant par son id, ou en crée un nouveau à partir du DTO si aucun ne correspond.
     * <p>
     * Limitation connue : certains ids IMDB apparaissent plusieurs fois dans la source JSON
     * (probablement un artefact de scraping, une occurrence par page de filmographie d'acteur visitée),
     * avec un {@code anneeSortie} différent à chaque occurrence mais un casting identique.
     * Comme cette méthode retourne dès la première occurrence trouvée, seule l'année associée
     * à cette première occurrence est conservée ; elle peut ne pas correspondre à l'année de début
     * réelle du film/show.
     *
     * @param dto le DTO contenant les données brutes du film
     * @return le Film trouvé ou nouvellement créé
     */
    public Film findOrCreate(FilmDto dto) {
        Optional<Film> filmExistant = filmDao.findById(dto.getId());
        if (filmExistant.isPresent()) {
            return filmExistant.get();
        }
        Integer anneeDebut = parseAnneeSortie(dto.getAnneeSortie());
        BigDecimal note = parseRating(dto.getRating());
        String resume = ParsingUtils.nullIfBlank(dto.getPlot());

        String raw = ParsingUtils.nullIfBlank(dto.getLangue());
        Langue langue = (raw == null) ? null : langueMapper.findOrCreate(raw);

        Pays pays = paysMapper.findOrCreate(dto.getPays().getNom());

        Set<Genre> genres = resolveGenres(dto.getGenres());
        Set<Personne> realisateurs = resolveRealisateurs(dto.getRealisateurs());

        Film film = new Film(dto.getId(), dto.getNom(), anneeDebut, note, resume, langue, pays, genres, realisateurs);
        filmDao.save(film);
        resolveRoles(dto, film);
        return film;
    }

    /**
     * Parse une note brute (format "x.x") en BigDecimal.
     * Retourne null si la chaîne est vide ou si le format ne peut pas être interprété
     *
     * @param raw la note brute telle qu'elle apparaît dans le JSON
     * @return le BigDecimal correspondant, ou null si non interprétable
     */
    private BigDecimal parseRating(String raw) {
        raw = ParsingUtils.nullIfBlank(raw);
        if (raw == null) {
            return null;
        }
        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parse une année de sortie brute en Integer à 4 chiffres exactement.
     * Quand le format brut est une plage ("1975-1979") on ne garde que le début.
     * Retourne null si la chaîne est vide ou comprend moins de 4 caractères.
     *
     * @param raw l'année de sortie brute telle qu'elle apparaît dans le JSON
     * @return l'Integer correspondant, ou null si non interprétable
     */
    private Integer parseAnneeSortie(String raw) {
        raw = ParsingUtils.nullIfBlank(raw);
        if (raw == null || raw.length() < 4) {
            return null;
        }
        String anneeDebut = raw.substring(0, 4);
        try {
            return Integer.parseInt(anneeDebut);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Résout chaque genre brut du JSON en entité Genre (recherche ou création),
     * et regroupe le résultat dans un Set.
     *
     * @param genresRaw la liste brute des noms de genres tels qu'ils apparaissent dans le JSON
     * @return le Set des entités Genre correspondantes, trouvées ou nouvellement créées
     */
    private Set<Genre> resolveGenres(List<String> genresRaw) {
        Set<Genre> genres = new HashSet<>();
        for (String nom : genresRaw) {
            Genre genre = genreMapper.findOrCreate(nom);
            genres.add(genre);
        }
        return genres;
    }

    /**
     * Résout chaque réalisateur brut du JSON en entité Personne (recherche ou création),
     * et regroupe le résultat dans un Set.
     *
     * @param realisateursRaw la liste brute des réalisateurs tels qu'ils apparaissent dans le JSON
     * @return le Set des entités Personne correspondantes, trouvées ou nouvellement créées
     */
    private Set<Personne> resolveRealisateurs(List<PersonneDto> realisateursRaw) {
        Set<Personne> realisateurs = new HashSet<>();
        for (PersonneDto realisateurDto : realisateursRaw) {
            Personne personne = personneMapper.findOrCreate(realisateurDto);
            realisateurs.add(personne);
        }
        return realisateurs;
    }

    /**
     * Résout les rôles d'un film (création et sauvegarde de chaque Role),
     * en déterminant pour chacun s'il appartient au casting principal
     * en comparant l'id de l'acteur à ceux du casting principal du DTO.
     * Ne renvoie rien : les Role sont sauvegardés directement via RoleMapper.
     *
     * @param dto  le DTO contenant les données brutes du film (rôles et casting principal)
     * @param film le Film déjà construit, auquel les rôles seront rattachés
     */
    private void resolveRoles(FilmDto dto, Film film) {
        Set<String> ids = new HashSet<>();
        for (PersonneDto personneDto : dto.getCastingPrincipal()) {
            String id = personneDto.getId();
            ids.add(id);
        }

        for (RoleDto roleDto : dto.getRoles()) {
            Personne acteur = personneMapper.findOrCreate(roleDto.getActeur());
            boolean principal = ids.contains(acteur.getId());
            roleMapper.toEntity(roleDto, acteur, film, principal);

        }
    }
}