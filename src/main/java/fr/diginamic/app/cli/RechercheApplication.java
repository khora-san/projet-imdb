package fr.diginamic.app.cli;

import fr.diginamic.dao.FilmDao;
import fr.diginamic.dao.PersonneDao;
import fr.diginamic.dao.RoleDao;
import fr.diginamic.entities.Film;
import fr.diginamic.entities.Personne;
import fr.diginamic.entities.Role;
import fr.diginamic.services.RechercheService;
import fr.diginamic.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Point d'entrée de l'application de recherche : propose un menu interactif
 * (via Scanner) permettant d'interroger la base de données déjà peuplée,
 * à travers les 6 recherches prédéfinies (filmographie d'un·e acteur·ice, casting
 * d'un film, films sur une période, films/acteur·ice·s communs, etc.).
 */
public class RechercheApplication {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();

        PersonneDao personneDao = new PersonneDao(em);
        FilmDao filmDao = new FilmDao(em);
        RoleDao roleDao = new RoleDao(em);
        RechercheService rechercheService = new RechercheService(roleDao, filmDao, personneDao);

        Scanner scanner = new Scanner(System.in);
        int choix;

        while (true) {
            System.out.println("\n========= INTERNET MOVIE DATABASE ========");
            System.out.println("1. Afficher la filmographie d'un·e acteur·ice");
            System.out.println("2. Afficher le casting d'un film");
            System.out.println("3. Afficher les films sortis entre deux années");
            System.out.println("4. Afficher les films communs à 2 acteur·ice·s");
            System.out.println("5. Afficher les acteur·ice·s communs à 2 films");
            System.out.println("6. Afficher les films sortis entre deux années avec un·e acteur·ice donné·e au casting");
            System.out.println("7. Quitter l'application");
            System.out.print("> ");
            choix = Integer.parseInt(scanner.nextLine());
            if (choix == 7) break;

            switch (choix) {
                // Afficher la filmographie d'un·e acteur·ice
                case 1: {
                    Optional<Personne> acteurOpt = choisirActeur(scanner, rechercheService);
                    if (acteurOpt.isPresent()) {
                        Personne acteur = acteurOpt.get();
                        List<Role> filmographie = rechercheService.rechercherFilmographie(acteur);
                        if (filmographie.isEmpty()) {
                            System.out.println("Aucune filmographie trouvée.");
                        } else {

                            for (Role role : filmographie) {
                                String personnageAffiche = (role.getPersonnage() != null)
                                        ? role.getPersonnage()
                                        : "personnage inconnu";
                                System.out.println(role.getFilm().getTitre() + " (" + role.getFilm().getAnneeDebut() + ") : " + personnageAffiche);
                            }
                        }
                    }
                }
                break;
                // Afficher le casting d'un film
                case 2: {
                    Optional<Film> filmOpt = choisirFilm(scanner, rechercheService);
                    if (filmOpt.isPresent()) {
                        Film film = filmOpt.get();
                        List<Role> casting = rechercheService.rechercherCasting(film);
                        if (casting.isEmpty()) {
                            System.out.println("Aucun casting trouvé.");
                        } else {

                            for (Role role : casting) {
                                String personnageAffiche = (role.getPersonnage() != null)
                                        ? role.getPersonnage()
                                        : "personnage inconnu";
                                System.out.println(role.getPersonne().getIdentite() + " (" + personnageAffiche + ")");
                            }
                        }
                    }
                }
                break;
                // Afficher les films sortis entre deux années
                case 3: {

                    System.out.print("Veuillez entrer deux années séparées par un espace : ");
                    String input = scanner.nextLine();
                    String[] parts = input.split("\\s+");
                    int min = Integer.parseInt(parts[0]);
                    int max = Integer.parseInt(parts[1]);
                    List<Film> films = rechercheService.rechercherFilmsEntreAnnees(min, max);
                    if (films.size() > 200) {
                        System.out.println(films.size() + " films trouvés — trop nombreux pour un affichage clair. Merci d'affiner la période.");
                    } else if (films.isEmpty()) {
                        System.out.println("Aucun film trouvé sur cette période.");
                    } else {
                        for (Film film : films) {
                            System.out.println(film.getTitre() + " (" + film.getAnneeDebut() + ")");
                        }

                    }

                }
                break;
                // Afficher les films communs à 2 acteur·ice·s
                case 4: {
                    Optional<Personne> acteurOpt1 = choisirActeur(scanner, rechercheService);
                    Optional<Personne> acteurOpt2 = choisirActeur(scanner, rechercheService);
                    if (acteurOpt1.isPresent() && acteurOpt2.isPresent()) {
                        Personne acteur1 = acteurOpt1.get();
                        Personne acteur2 = acteurOpt2.get();
                        List<Film> filmoCommune = rechercheService.rechercherFilmsCommuns(acteur1, acteur2);

                        if (filmoCommune.isEmpty()) {
                            System.out.println("Aucun film commun trouvé.");
                        } else {
                            for (Film film : filmoCommune) {
                                System.out.println(film.getTitre() + " (" + film.getAnneeDebut() + ")");
                            }
                        }
                    }
                }
                break;
                // Afficher les acteur·ice·s commun·e·s à 2 films
                case 5: {
                    Optional<Film> filmOpt1 = choisirFilm(scanner, rechercheService);
                    Optional<Film> filmOpt2 = choisirFilm(scanner, rechercheService);
                    if (filmOpt1.isPresent() && filmOpt2.isPresent()) {
                        Film film1 = filmOpt1.get();
                        Film film2 = filmOpt2.get();
                        List<Personne> acteursCommuns = rechercheService.rechercherActeursCommuns(film1, film2);

                        if (acteursCommuns.isEmpty()) {
                            System.out.println("Aucun·e acteur·ice commun·e trouvé·e.");
                        } else {
                            for (Personne personne : acteursCommuns) {
                                System.out.println(personne.getIdentite());
                            }
                        }
                    }
                }
                break;
                // Afficher les films sortis entre deux années avec un·e acteur·ice donné·e au casting
                case 6: {
                    Optional<Personne> acteurOpt = choisirActeur(scanner, rechercheService);
                    if (acteurOpt.isPresent()) {
                        Personne acteur = acteurOpt.get();
                        System.out.print("Veuillez entrer deux années séparées par un espace : ");
                        String input = scanner.nextLine();
                        String[] parts = input.split("\\s+");
                        int min = Integer.parseInt(parts[0]);
                        int max = Integer.parseInt(parts[1]);
                        List<Film> films = rechercheService.rechercherFilmsEntreAnneesAvecActeur(acteur, min, max);

                        if (films.size() > 200) {
                            System.out.println(films.size() + " films trouvés — trop nombreux pour un affichage clair. Merci d'affiner la période.");
                        } else if (films.isEmpty()) {
                            System.out.println("Aucun film trouvé pour cet·te acteur·ice sur cette période.");
                        } else {
                            for (Film film : films) {
                                System.out.println(film.getTitre() + " (" + film.getAnneeDebut() + ")");
                            }
                        }
                    }
                }
                break;
                default:
                    System.out.println("Option invalide, réessayez.");
                    break;
            }
        }

    }

    /**
     * Demande à l'utilisateur un nom d'acteur·ice, recherche les correspondances en base,
     * et fait choisir l'utilisateur parmi les homonymes éventuels via un sous-menu numéroté.
     *
     * @param scanner          le Scanner partagé utilisé pour lire la saisie utilisateur
     * @param rechercheService le service utilisé pour rechercher les acteur·ice·s par nom
     * @return un Optional contenant la Personne choisie, ou Optional.empty() si aucun·e acteur·ice·s ne correspond au nom saisi
     */
    private static Optional<Personne> choisirActeur(Scanner scanner, RechercheService rechercheService) {
        System.out.print("Nom de l'acteur·ice : ");
        String nom = scanner.nextLine();
        List<Personne> personnes = rechercheService.rechercherActeursParNom(nom);
        if (personnes.isEmpty()) {
            System.out.println("Le nom de l'acteur·ice n'existe pas.");
            return Optional.empty();
        }
        if (personnes.size() == 1) {
            return Optional.of(personnes.getFirst());
        } else {
            int i = 1;
            for (Personne personne : personnes) {
                String dateAffichee = (personne.getDateNaissance() != null)
                        ? personne.getDateNaissance().toString()
                        : "date de naissance inconnue";
                System.out.println(i + ". " + personne.getIdentite() + ", " + dateAffichee);
                i++;
            }
            int choix = Integer.parseInt(scanner.nextLine());
            return Optional.of(personnes.get(choix - 1));
        }
    }

    /**
     * Demande à l'utilisateur un titre de film, recherche les correspondances en base,
     * et fait choisir l'utilisateur parmi les homonymes éventuels via un sous-menu numéroté.
     *
     * @param scanner          le Scanner partagé utilisé pour lire la saisie utilisateur
     * @param rechercheService le service utilisé pour rechercher les films par titre
     * @return un Optional contenant le Film choisi, ou Optional.empty() si aucun film ne correspond au titre saisi
     */
    private static Optional<Film> choisirFilm(Scanner scanner, RechercheService rechercheService) {
        System.out.print("Nom du Film : ");
        String titre = scanner.nextLine();
        List<Film> films = rechercheService.rechercherFilmsParTitre(titre);
        if (films.isEmpty()) {
            System.out.println("Le nom du film n'existe pas.");
            return Optional.empty();
        }
        if (films.size() == 1) {
            return Optional.of(films.getFirst());
        } else {
            int i = 1;
            for (Film film : films) {
                String anneeAffichee = (film.getAnneeDebut() != null)
                        ? film.getAnneeDebut().toString()
                        : "année de sortie inconnue";
                System.out.println(i + ". " + anneeAffichee);
                i++;
            }
            int choix = Integer.parseInt(scanner.nextLine());
            return Optional.of(films.get(choix - 1));
        }
    }

}


