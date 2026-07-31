# projet-imdb

![version](https://img.shields.io/github/v/tag/khora-san/projet-imdb)

Application Java / JPA (Hibernate) + MariaDB permettant d'importer un jeu de données de films et de personnes issu d'
IMDB (source JSON) en base relationnelle, puis d'interroger cette base via une application en ligne de commande
proposant six recherches prédéfinies.

Projet réalisé dans le cadre d'une formation JPA/Hibernate, avec un double objectif : mettre en pratique le mapping
objet-relationnel (entités, relations, DAO), et manipuler une base de données réelle de taille non triviale (~2700
films, casting complet) avec ses problèmes de qualité de données concrets (valeurs manquantes, doublons, formats
hétérogènes).

## Sommaire

- [Prérequis](#prérequis)
- [Installation et configuration](#installation-et-configuration)
- [Structure du projet](#structure-du-projet)
- [Architecture](#architecture)
- [Modèle de données](#modèle-de-données)
- [Application d'import](#application-dimport)
- [Application de recherche](#application-de-recherche)
- [Choix techniques](#choix-techniques)
- [Limitations connues](#limitations-connues)
- [Conventions du projet](#conventions-du-projet)
- [Pistes d'amélioration](#pistes-damélioration)

## Prérequis

- Java 21 (JDK)
- MariaDB (testé via XAMPP-Lite, port par défaut 3306)
- Un IDE compatible Maven (IntelliJ recommandé) — le projet est un projet Maven standard, mais IntelliJ embarque son
  propre Maven ("Bundled Maven") et résout les dépendances automatiquement à l'ouverture du `pom.xml`. Aucune
  installation séparée de Maven n'est nécessaire, et il n'y a pas de commande à lancer manuellement (type
  `mvn clean install`) pour ce projet mono-module exécuté directement depuis l'IDE.

## Installation et configuration

1. Démarrer Apache + MariaDB via XAMPP-Lite.
2. Créer une base nommée `imdb`.
3. Exécuter le script `sql/schema.sql` pour créer les tables.
4. Adapter, si besoin, les paramètres de connexion dans `persistence.xml` — par défaut, identifiants XAMPP standards (
   `root`, sans mot de passe).
5. Le fichier source `films.json` (~20 Mo) est commité dans le repo, en ressource classpath (`/films.json`) — aucun
   téléchargement séparé n'est nécessaire.
6. Ouvrir le projet dans l'IDE : les dépendances Maven sont résolues automatiquement, aucune compilation manuelle
   requise.

### Ordre de lancement

1. Exécuter `ImportApplication` (`fr.diginamic.app`) une première fois pour peupler la base à partir de `films.json`.
   Cette étape prend actuellement environ 34 secondes (voir [Choix techniques](#choix-techniques) pour le détail du
   cache ayant permis de réduire ce temps depuis 80 secondes). Elle est idempotente : la relancer sur une base déjà
   peuplée ne duplique pas les données déjà importées.
2. Exécuter `RechercheApplication` (`fr.diginamic.app.cli`) pour interroger la base via le menu interactif.

Le script `sql/check-homonymes.sql` permet, indépendamment de l'application, d'identifier en base les personnes
partageant la même identité (voir [Limitations connues](#limitations-connues)).

## Structure du projet

```
projet-imdb/
├── conception/              # diagramme de classes UML + MPD (.jpg)
├── sql/
│   ├── schema.sql           # (re)création des tables
│   └── check-homonymes.sql  # requête de diagnostic des homonymes
├── src/main/java
│   └── fr
│       └── diginamic
│           ├── app
│           │   └── cli
│           ├── dao
│           ├── dto
│           ├── entities
│           ├── mapper
│           ├── services
│           └── util
└── pom.xml
```

- **`app`** : point d'entrée de l'application d'import (`ImportApplication`).
- **`app.cli`** : point d'entrée de l'application de recherche, avec son menu interactif (`RechercheApplication`).
- **`dao`** : accès aux données via JPA/JPQL (une classe par entité — `FilmDao`, `PersonneDao`, `RoleDao`, `GenreDao`,
  `LangueDao`, `PaysDao`, `LieuNaissanceDao`).
- **`dto`** : structures brutes reflétant le JSON source (`FilmDto`, `PersonneDto`, `RoleDto`, `PaysDto`,
  `NaissanceDto`), avant toute transformation en entité.
- **`entities`** : le modèle JPA (`Film`, `Personne`, `Role`, `Genre`, `Langue`, `Pays`, `LieuNaissance`).
- **`mapper`** : transformation DTO → entité, avec logique de résolution "find or create" (`FilmMapper`,
  `PersonneMapper`, `RoleMapper`, `GenreMapper`, `LangueMapper`, `PaysMapper`, `LieuNaissanceMapper`).
- **`services`** : orchestration métier au-dessus des DAO/Mapper (`ImportService`, `RechercheService`).
- **`util`** : utilitaires transverses (`JpaUtil` pour la gestion de l'`EntityManager`, `ParsingUtils` pour le nettoyage
  de chaînes brutes).

## Architecture

Le projet suit une architecture en couches strictes, chaque couche n'ayant connaissance que de celle immédiatement en
dessous :

```
Application (CLI)  →  Service  →  Mapper  →  DAO  →  Entités JPA
                                     ↓
                                    DTO (uniquement côté import)
```

- Les applications (`ImportApplication`, `RechercheApplication`) ne parlent jamais directement aux DAO : elles passent
  systématiquement par un service.
- `RechercheService` reste volontairement une couche fine : certaines de ses méthodes ne font qu'une délégation directe
  vers un DAO. Ce choix n'est pas de la sur-ingénierie mais une discipline de couches : garder cette séparation permet
  de faire évoluer la couche de persistance (DAO/JPQL) sans jamais toucher au CLI.
- Côté import, les DTO existent pour isoler la structure brute du JSON (types `String` partout, y compris pour des
  champs numériques comme `rating` ou `anneeSortie`) du modèle métier typé. Le parsing/la validation sont délégués aux
  Mapper, jamais faits dans les DTO eux-mêmes.
- Chaque Mapper de référence (`Pays`, `Langue`, `Genre`, `LieuNaissance`) expose une méthode `findOrCreate` : recherche
  par valeur métier (nom/libellé), création si absente. C'est le point d'entrée unique pour résoudre ces entités de
  référence depuis l'import.

## Modèle de données

Entités principales et leurs relations :

- **`Film`** : identifiant IMDB (`String`), titre, année de début, note, résumé, une `Langue` (optionnelle), un `Pays` (
  obligatoire), plusieurs `Genre`, plusieurs `Personne` en tant que réalisateurs.
- **`Personne`** : identifiant IMDB, identité, date de naissance, un `LieuNaissance` (optionnel).
- **`Role`** : entité d'association entre `Film` et `Personne`, portant les attributs propres au rôle (personnage joué,
  appartenance ou non au casting principal).
- **`Genre`**, **`Langue`**, **`Pays`**, **`LieuNaissance`** : entités de référence, résolues par nom/libellé (
  voir [Choix techniques](#choix-techniques)).

Le détail complet (cardinalités, clés, contraintes) est disponible dans `conception/` (diagramme UML + MPD).

## Application d'import

`ImportApplication` orchestre, via `ImportService`, le pipeline suivant :

1. **Parsing** : lecture de `films.json` et désérialisation Jackson vers une liste de `FilmDto`.
2. **Persistance film par film**, chacun dans sa propre transaction JPA (`RESOURCE_LOCAL`, donc `begin()`/`commit()`
   explicites). Ce choix — plutôt qu'une transaction unique pour tout l'import — permet à un échec isolé sur un film de
   ne pas annuler les films déjà importés avec succès : en cas d'exception, seule la transaction du film concerné est
   annulée (`rollback()`), et l'import continue avec les suivants.
3. **Résolution des entités liées** par `FilmMapper`, qui délègue à un Mapper dédié par type de donnée (pays, langue,
   genres, réalisateurs, casting).
4. **Bilan console** en fin d'import : nombre de films traités avec succès sur le total, nombre de films réellement
   ajoutés (distinct du nombre traités, car un id IMDB dupliqué dans le JSON source est retrouvé et non recréé), durée
   totale.

## Application de recherche

`RechercheApplication` propose un menu interactif (`Scanner`) avec six recherches prédéfinies :

1. Filmographie d'un·e acteur·ice
2. Casting d'un film
3. Films sortis entre deux années
4. Films communs à deux acteur·ice·s
5. Acteur·ice·s communs à deux films
6. Films sortis entre deux années avec un·e acteur·ice donné·e au casting

Les recherches par nom ou titre (acteur·ice, film) passent par une méthode de désambiguïsation partagée (
`choisirActeur`/`choisirFilm`) : en cas de résultat unique, il est retourné directement ; en cas d'homonymes, un
sous-menu numéroté (avec date de naissance ou année de sortie pour aider à distinguer) permet de choisir la bonne
entrée.

## Choix techniques

- **Recherche insensible à la casse, pas de recherche floue** : toutes les recherches par nom/titre utilisent `LOWER()`
  des deux côtés de la comparaison JPQL (`WHERE LOWER(f.titre) = LOWER(:titre)`). Il s'agit d'une correspondance exacte
  après normalisation de casse, pas d'une recherche par fragment (`LIKE '%...%'`).
- **Transactions par film** à l'import (voir [Application d'import](#application-dimport)), pour préserver la
  progression déjà accomplie en cas d'échec ponctuel.
- **Règle métier explicite "pays inconnu"** : un film sans pays renseigné dans le JSON n'est pas importé (
  `IllegalArgumentException` levée intentionnellement dans `FilmMapper.findOrCreate`), plutôt que de laisser échouer
  silencieusement sur une `NullPointerException`. Concerne 18 films sur 2748 dans le jeu de données actuel.
- **Cache mémoire sur les Mapper de référence** (`PaysMapper`, `LangueMapper`, `GenreMapper`, `LieuNaissanceMapper`) :
  chacun maintient une `Map<String, T>` interne, peuplée au fil de l'import. Ce cache est nécessaire car le cache de
  premier niveau de Hibernate (contexte de persistance) n'accélère que les accès par clé primaire (`em.find`), pas les
  requêtes JPQL avec clause `WHERE` utilisées par ces `findOrCreate`. Ce cache a réduit le temps d'import complet de 80
  à 34 secondes.
- **Normalisation ciblée de données brutes**, appliquée au niveau du Mapper concerné plutôt que dans l'utilitaire
  générique `ParsingUtils.nullIfBlank` : la valeur littérale `"None"` dans le champ langue (probablement un `None`
  Python sérialisé en texte) est traitée comme une absence de langue ; les notes au format français avec virgule
  décimale (ex. `"8,7"`) sont normalisées en point avant parsing en `BigDecimal`.
- **Écriture inclusive** : les sorties console et la Javadoc en français utilisent le point médian (`acteur·ice`,
  `acteur·ice·s`).

## Limitations connues

- **Imprécision possible de l'année de sortie** sur les films dont l'id IMDB apparaît plusieurs fois dans le JSON
  source (artefact de scraping — une occurrence par page de filmographie d'acteur visitée). Seule la première occurrence
  rencontrée fait foi ; le casting associé est identique entre occurrences, seule l'année diffère.
- **Homonymes non résolus** : 116 couples de personnes partageant la même identité (332 personnes), une partie sans date
  de naissance exploitable pour les distinguer dans le sous-menu de désambiguïsation.
- **Doublons sémantiques de `LieuNaissance`** : la correspondance `LOWER()` corrige les doublons de casse/espaces, mais
  pas les vrais doublons sémantiques (variantes orthographiques français/anglais, niveaux de détail administratif
  différents, annotations historiques). Une résolution propre nécessiterait un référentiel de lieux normalisés ou une
  API de géocodage, hors périmètre de ce projet.
- **Pas de vérification anti-duplication composite sur `Role`** (film + personne + personnage) : aucune protection
  actuelle contre la création de rôles en double.
- **Validation de saisie utilisateur limitée** côté CLI : les indices hors limites ou formats non respectés dans les
  saisies combinées (ex. deux années séparées par un espace) ne sont pas gérés défensivement.

La liste complète et détaillée des points reportés est tenue à jour dans `AMELIORATIONS_V2.md`.

## Conventions du projet

- **Javadoc systématique** sur les classes, constructeurs et méthodes, y compris les entités et DTO.
- **Écriture inclusive** au point médian dans les sorties console et la Javadoc en français.
- **Git** : workflow `main` / `dev` / `feat/*`, intégration par `merge` (pas de `rebase`), tags posés aux étapes clés (
  ex. avant suppression des classes de test, versions stables).

## Pistes d'amélioration

Voir `AMELIORATIONS_V2.md` pour le backlog complet et détaillé (modèle de données, tests, documentation, import,
recherche/CLI, qualité des données, infrastructure).

## Dépendances principales

| Dépendance          | Version |
|---------------------|---------|
| mariadb-java-client | 3.5.9   |
| jackson-databind    | 2.22.0  |
| hibernate-core      | 7.4.4   |
| lombok              | 1.18.42 |
