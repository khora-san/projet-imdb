package fr.diginamic;

import fr.diginamic.util.JpaUtil;

/**
 * Classe de test temporaire, destinée à être supprimée une fois la vérification effectuée.
 * Déclenche le chargement de JpaUtil (donc la création de l'EntityManagerFactory) afin de
 * valider que le mapping des entités correspond bien au schéma réel de la base de données.
 */
public class TestBootstrap {
    public static void main(String[] args) {
        JpaUtil.close();
    }
}
