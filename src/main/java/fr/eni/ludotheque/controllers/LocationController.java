package fr.eni.ludotheque.controllers;

import fr.eni.ludotheque.bo.ExemplaireJeu;
import fr.eni.ludotheque.bo.Location;
import fr.eni.ludotheque.services.ExemplaireJeuService;
import fr.eni.ludotheque.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final ExemplaireJeuService exemplaireJeuService;

    public LocationController(LocationService locationService, ExemplaireJeuService exemplaireJeuService) {
        this.locationService = locationService;
        this.exemplaireJeuService = exemplaireJeuService;
    }

    /*
     * Afficher la liste des locations
     */
    @GetMapping
    public String locations(Model model) {
        List<Location> locations = locationService.findAll();

        for (Location location : locations) {
            // Récupérez les informations nécessaires
            ExemplaireJeu exemplaire = location.getExemplaire();
            if (exemplaire != null && exemplaire.getJeu() != null) {
                double tarifJournalier = exemplaire.getJeu().getTarifJournee();
                double prixTotal = locationService.calculerPrixTotal(location.getDateDebut(), location.getDateFin(), tarifJournalier);
                location.setPrixTotal(prixTotal); // Mettez à jour le prix total
            } else {
                location.setPrixTotal(0.0); // Défaut si les données sont incomplètes
            }
        }

        model.addAttribute("locations", locations);
        model.addAttribute("body", "locations/liste");
        return "index";
    }



    /*
     * Afficher le formulaire pour ajouter une location
     */
    @GetMapping("/ajouter")
    public String pageAjouterLocation(Model model) {
        model.addAttribute("location", new Location());
        model.addAttribute("exemplaires", exemplaireJeuService.findAll());
        model.addAttribute("body", "locations/formulaire-location");
        return "index";
    }

    /*
     * Enregistrer une nouvelle location
     */
    @PostMapping("/enregistrer")
    public String ajouterOuModifierLocation(@Valid Location location, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || location.getExemplaire() == null || location.getExemplaire().getJeu() == null) {
            model.addAttribute("exemplaires", exemplaireJeuService.findAll());
            model.addAttribute("body", "locations/formulaire-location");
            return "index";
        }

        // Calcul du prix total
        double tarifJournalier = location.getExemplaire().getJeu().getTarifJournee();
        double prixTotal = locationService.calculerPrixTotal(location.getDateDebut(), location.getDateFin(), tarifJournalier);
        location.setPrixTotal(prixTotal);

        locationService.save(location);
        return "redirect:/locations";
    }


    /*
     * Afficher le formulaire pour modifier une location
     */
    @GetMapping("/modifier/{id}")
    public String pageModifierLocation(@PathVariable("id") int id, Model model) {
        Optional<Location> locationOpt = locationService.findById(id);
        if (locationOpt.isPresent()) {
            model.addAttribute("location", locationOpt.get());
            model.addAttribute("exemplaires", exemplaireJeuService.findAll());
            model.addAttribute("body", "locations/formulaire-location");
            return "index";
        } else {
            model.addAttribute("errorMessage", "Location introuvable.");
            return "redirect:/locations";
        }
    }

    /*
     * Supprimer une location
     */
    @GetMapping("/supprimer/{id}")
    public String supprimerLocation(@PathVariable("id") int id) {
        locationService.delete(id);
        return "redirect:/locations";
    }
}
