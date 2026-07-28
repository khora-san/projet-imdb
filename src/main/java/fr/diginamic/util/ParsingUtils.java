package fr.diginamic.util;

/**
 * Regroupe des méthodes utilitaires de parsing
 * utilisées dans les Mapper
 */
public class ParsingUtils {

    /**
     * Classe jamais instanciée
     */
    private ParsingUtils() {
    }

    /**
     *
     * @param s la string à parser
     * @return la chaîne trimée,
     * ou null si elle est nulle, vide, ou ne contient que des espaces
     */
    public static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
