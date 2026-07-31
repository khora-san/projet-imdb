# Améliorations reportées (backlog v2)

Ce fichier recense les décisions de conception mises de côté au fil du développement de la v1 du projet IMDB, pour ne
pas les oublier lors d'une itération future. Chaque point a fait l'objet d'une décision consciente (pas un oubli) —
reporté par souci de rester dans le périmètre de la v1.

## Modèle de données / Entités

- `LieuTournage` en `@Embeddable` (reporté à v2)
- Vérification composite anti-duplication sur `Role` (combinaison film + personne + personnage), actuellement aucune
  protection contre la création de rôles en double

## Tests, intégration continue et conteneurisation

- `equals()`/`hashCode()`/`@AllArgsConstructor` sur les DTO
- Tests unitaires sur `RechercheService`/`ImportService`, DAO mockés, pour valider la logique métier indépendamment de la base
- Tests d'intégration sur la couche DAO, pour valider que les requêtes JPQL renvoient bien les résultats attendus (base de test dédiée ou H2 en mémoire)
- Intégration continue via GitHub Actions : build + exécution des tests à chaque push, pour détecter une régression avant merge sur `dev`/`main`
- Conteneurisation de MariaDB via Docker/`docker-compose` (avec exécution automatique de `sql/schema.sql` au démarrage), pour se passer de XAMPP-Lite en local — alternative plus simple à la migration H2 déjà notée en Infrastructure, pour le même bénéfice de confort de développement
- Piste plus avancée : Testcontainers pour les tests d'intégration, démarrant une vraie instance MariaDB éphémère en conteneur (plus fidèle qu'H2, isolé et reproductible en CI)

## Import (ImportService / FilmMapper)

- Amélioration du parsing de `dateNaissance` pour couvrir plus de formats bruts rencontrés dans le JSON (ex. dates sans
  année, comme `"April 25 "` pour Sara Paxton)
- Limitation connue déjà documentée en Javadoc : imprécision possible de `anneeDebut` sur les films dont l'id IMDB
  apparaît plusieurs fois dans le JSON (doublons de scraping) — seule la première occurrence rencontrée fait foi

## Recherche / CLI (RechercheService / RechercheApplication)

- Validation de saisie utilisateur : indices hors limites dans `choisirActeur`/`choisirFilm` (
  `IndexOutOfBoundsException` non gérée), formats non respectés dans les saisies combinées (ex. `case 3`,
  `ArrayIndexOutOfBoundsException`/`NumberFormatException` non gérées)
- Stratégie de désambiguïsation des homonymes `Personne` quand `dateNaissance` est `NULL` — 332 personnes concernées par
  des homonymes, dont une bonne partie sans date de naissance exploitable pour les distinguer
- Affichage de la langue (en plus de l'année) dans le sous-menu de désambiguïsation de `choisirFilm`
- Paramètre de label personnalisé pour `choisirActeur`/`choisirFilm`, pour distinguer "premier acteur"/"second acteur" (
  ou "premier film"/"second film") à l'affichage, plutôt que le même message répété
- Gestion des apostrophes courbes/droites et des accents dans les recherches par nom/titre (au-delà de la simple
  insensibilité à la casse déjà gérée avec `LOWER()`)
- Extraction éventuelle d'une classe `ConsoleMenu` séparée, si `RechercheApplication` devient trop volumineuse à mesure
  que les 6 recherches sont implémentées