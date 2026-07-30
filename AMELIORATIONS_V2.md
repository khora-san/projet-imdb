# Améliorations reportées (backlog v2)

Ce fichier recense les décisions de conception mises de côté au fil du développement de la v1 du projet IMDB, pour ne pas les oublier lors d'une itération future. Chaque point a fait l'objet d'une décision consciente (pas un oubli) — reporté par souci de rester dans le périmètre de la v1.

## Modèle de données / Entités

- `LieuTournage` en `@Embeddable` (reporté à v1.1/v2)
- `Film.getAnciennete()`, méthode métier calculant l'ancienneté d'un film (après la v1)
- Vérification composite anti-duplication sur `Role` (combinaison film + personne + personnage), actuellement aucune protection contre la création de rôles en double

## DTO / Tests

- `equals()`/`hashCode()`/`@AllArgsConstructor` sur les DTO — utiles principalement pour des tests unitaires, aucun test unitaire prévu en v1

## Documentation

- Javadoc rétroactive sur les entités écrites avant que la convention "Javadoc systématique" soit adoptée en cours de projet : `Personne`, `LieuNaissance`, `Pays`, `Langue`, `Genre`, `Role`

## Import (ImportService / FilmMapper)

- Amélioration du parsing de `dateNaissance` pour couvrir plus de formats bruts rencontrés dans le JSON (ex. dates sans année, comme `"April 25 "` pour Sara Paxton)
- Limitation connue déjà documentée en Javadoc : imprécision possible de `anneeDebut` sur les films dont l'id IMDB apparaît plusieurs fois dans le JSON (doublons de scraping) — seule la première occurrence rencontrée fait foi

## Recherche / CLI (RechercheService / RechercheApplication)

- Validation de saisie utilisateur : indices hors limites dans `choisirActeur`/`choisirFilm` (`IndexOutOfBoundsException` non gérée), formats non respectés dans les saisies combinées (ex. `case 3`, `ArrayIndexOutOfBoundsException`/`NumberFormatException` non gérées)
- Stratégie de désambiguïsation des homonymes `Personne` quand `dateNaissance` est `NULL` — 332 personnes concernées par des homonymes, dont une bonne partie sans date de naissance exploitable pour les distinguer
- Affichage de la langue (en plus de l'année) dans le sous-menu de désambiguïsation de `choisirFilm`
- Paramètre de label personnalisé pour `choisirActeur`/`choisirFilm`, pour distinguer "premier acteur"/"second acteur" (ou "premier film"/"second film") à l'affichage, plutôt que le même message répété
- Gestion des apostrophes courbes/droites et des accents dans les recherches par nom/titre (au-delà de la simple insensibilité à la casse déjà gérée avec `LOWER()`)
- Extraction éventuelle d'une classe `ConsoleMenu` séparée, si `RechercheApplication` devient trop volumineuse à mesure que les 6 recherches sont implémentées

## Git / Process

- Poser le tag git `avant-nettoyage-tests` une fois la v1 terminée et juste avant de supprimer `TestBootstrap`/`TestParsing`, pour garder une trace facilement accessible de l'état du projet avec ces classes de test encore présentes

## Checklist avant de qualifier une version "v1" (vs "v0.9")

- [ ] Menu CLI complet (6 recherches + option de sortie) fonctionnel de bout en bout
- [ ] Javadoc complète sur toutes les classes, constructeurs et méthodes publiques
- [ ] Nettoyage des classes de test (`TestBootstrap`, `TestParsing`) — après avoir posé le tag git `avant-nettoyage-tests`
- [ ] Dossier `conception/` (diagramme de classes UML + MPD) committé à la racine du repo
