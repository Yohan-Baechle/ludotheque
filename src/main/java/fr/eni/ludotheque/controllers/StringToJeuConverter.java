package fr.eni.ludotheque.controllers;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.services.JeuService;

@Component
public class StringToJeuConverter implements Converter<String, Jeu> {

    private final JeuService jeuService;

    public StringToJeuConverter(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    @Override
    public Jeu convert(String strJeuId) {
        int jeuId;
        try {
            jeuId = Integer.parseInt(strJeuId);
        } catch (NumberFormatException exc) {
            throw new IllegalArgumentException("Invalid jeu ID: " + strJeuId, exc);
        }

        Optional<Jeu> optJeu = jeuService.findById(jeuId);
        return optJeu.orElseThrow(() -> new RuntimeException("Jeu with ID " + jeuId + " not found"));
    }
}
