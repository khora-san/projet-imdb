package fr.diginamic.mapper;

import fr.diginamic.dao.RoleDao;
import fr.diginamic.dto.RoleDto;
import fr.diginamic.entities.Film;
import fr.diginamic.entities.Personne;
import fr.diginamic.entities.Role;

/**
 * Construit et sauvegarde une entité Role à partir de ses composants déjà résolus
 * (personnage, acteur, film, indicateur de casting principal).
 */
public class RoleMapper {

    private final RoleDao roleDao;

    /**
     * @param roleDao le DAO utilisé pour sauvegarder les rôles
     */
    public RoleMapper(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * Construit un Role à partir des éléments déjà résolus, le sauvegarde, puis le retourne.
     *
     * @param dto     le DTO contenant le nom du personnage
     * @param acteur  la Personne déjà résolue (trouvée ou créée) jouant ce rôle
     * @param film    le Film déjà en cours de construction auquel ce rôle appartient
     * @param principal indique si cet acteur fait partie du casting principal de ce film
     * @return le Role nouvellement créé et sauvegardé
     */
    public Role toEntity(RoleDto dto, Personne acteur, Film film, boolean principal) {
        Role role = new Role();
        role.setPersonnage(dto.getCharacterName());
        role.setPrincipal(principal);
        role.setFilm(film);
        role.setPersonne(acteur);
        roleDao.save(role);
        return role;
    }
}