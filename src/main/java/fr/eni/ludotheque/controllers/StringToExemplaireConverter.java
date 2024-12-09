package fr.eni.ludotheque.controllers;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.ludotheque.bll.ExemplaireService;
import fr.eni.ludotheque.bo.Exemplaire;

@Component
public class StringToExemplaireConverter implements Converter<String, Exemplaire> {

    private final ExemplaireService exemplaireJeuService;

    public StringToExemplaireConverter(ExemplaireService exemplaireJeuService) {
        this.exemplaireJeuService = exemplaireJeuService;
    }

    @Override
    public Exemplaire convert(String strExemplaireId) {
        int exemplaireId;
        try {
            exemplaireId = Integer.parseInt(strExemplaireId);
        } catch (NumberFormatException exc) {
            throw new IllegalArgumentException("Invalid exemplaire ID: " + strExemplaireId, exc);
        }

        Optional<Exemplaire> optExemplaire = exemplaireJeuService.findById(exemplaireId);
        return optExemplaire.orElseThrow(() -> new RuntimeException("ExemplaireJeu with ID " + exemplaireId + " not found"));
    }
}
