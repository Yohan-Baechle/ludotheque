package fr.eni.ludotheque.controllers;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.ludotheque.bll.GenreService;
import fr.eni.ludotheque.bo.Genre;

@Component
public class StringToGenreConverter implements Converter<String, Genre> {

    private final GenreService genreService;

    public StringToGenreConverter(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    public Genre convert(String strGenreId) {
        int genreId;
        try {
            genreId = Integer.parseInt(strGenreId);
        } catch (NumberFormatException exc) {
            throw new IllegalArgumentException("Invalid genre ID: " + strGenreId, exc);
        }

        Optional<Genre> optGenre = genreService.findById(genreId);
        return optGenre.orElseThrow(() -> new RuntimeException("Genre with ID " + genreId + " not found"));
    }
}
