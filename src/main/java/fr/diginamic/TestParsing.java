package fr.diginamic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.dto.FilmDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestParsing {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectmapper = new ObjectMapper();
        InputStream inputStream = TestParsing.class.getResourceAsStream("/films.json");

        List<FilmDto> films = objectmapper.readValue(inputStream, new TypeReference<List<FilmDto>>() {
        });

        System.out.println("Nombre de films : " + films.size());
        System.out.println(films.get(0).getNom());
        System.out.println(films.get(0).getPays().getNom());
        System.out.println(films.get(0).getRoles().size());
        System.out.println(films.get(0).getRoles().get(0).getCharacterName());
        System.out.println(films.get(0).getRoles().get(0).getActeur().getIdentite());
        System.out.println(films.get(0).getRoles().get(0).getActeur().getNaissance().getLieuNaissance());
    }
}
