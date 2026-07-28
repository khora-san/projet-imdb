DROP TABLE IF EXISTS ROLE, PERSONNE_FILM, FILM_GENRE;
DROP TABLE IF EXISTS PERSONNE, FILM;
DROP TABLE IF EXISTS LIEU_NAISSANCE, PAYS, LANGUE, GENRE;

-- Niveau 0 : aucune dépendance

CREATE TABLE LIEU_NAISSANCE (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    libelle VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_lieu_naissance_libelle (libelle)
);

CREATE TABLE PAYS (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_pays_nom (nom)
);

CREATE TABLE LANGUE (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_langue_nom (nom)
);

CREATE TABLE GENRE (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_genre_nom (nom)
);

-- Niveau 1 : dépend du niveau 0

CREATE TABLE PERSONNE (
    id VARCHAR(15) NOT NULL,
    identite VARCHAR(255) NOT NULL,
    date_naissance DATE NULL,
    lieu_naissance_id BIGINT(20) NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (lieu_naissance_id) REFERENCES LIEU_NAISSANCE(id)
);

CREATE TABLE FILM (
    id VARCHAR(15) NOT NULL,
    titre VARCHAR(500) NOT NULL,
    annee_debut INT(10) NOT NULL,
    note DECIMAL(3,1) NULL,
    resume TEXT NULL,
    langue_id BIGINT(20) NULL,
    pays_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (langue_id) REFERENCES LANGUE(id),
    FOREIGN KEY (pays_id) REFERENCES PAYS(id)
);

-- Niveau 2 : dépend des niveau 1 et 0

CREATE TABLE ROLE (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    personnage VARCHAR(255) NULL,
    principal TINYINT(1) NOT NULL,
    film_id VARCHAR(15) NOT NULL,
    personne_id VARCHAR(15) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (film_id) REFERENCES FILM(id),
    FOREIGN KEY (personne_id) REFERENCES PERSONNE(id)
);

CREATE TABLE PERSONNE_FILM (
    personne_id VARCHAR(15) NOT NULL,
    film_id VARCHAR(15) NOT NULL,
    PRIMARY KEY (personne_id, film_id),
    FOREIGN KEY (personne_id) REFERENCES PERSONNE(id),
    FOREIGN KEY (film_id) REFERENCES FILM(id)
);

CREATE TABLE FILM_GENRE (
    film_id VARCHAR(15) NOT NULL,
    genre_id BIGINT(20) NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES FILM(id),
    FOREIGN KEY (genre_id) REFERENCES GENRE(id)
);