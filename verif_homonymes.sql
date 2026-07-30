SELECT titre, COUNT(*) AS nb
FROM FILM
GROUP BY titre
HAVING COUNT(*) > 1
ORDER BY nb DESC;

SELECT titre, annee_debut
FROM FILM
WHERE titre IN (
    SELECT titre
    FROM FILM
    GROUP BY titre
    HAVING COUNT(*) > 1
)
ORDER BY titre, annee_debut;

SELECT identite, COUNT(*) AS nb
FROM PERSONNE
GROUP BY identite
HAVING COUNT(*) > 1
ORDER BY nb DESC;

SELECT identite, date_naissance
FROM PERSONNE
WHERE identite IN (
    SELECT identite
    FROM PERSONNE
    GROUP BY identite
    HAVING COUNT(*) > 1
)
ORDER BY identite, date_naissance;