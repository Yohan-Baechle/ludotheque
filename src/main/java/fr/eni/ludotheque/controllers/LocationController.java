package fr.eni.ludotheque.controllers;

import fr.eni.ludotheque.bll.ExemplaireService;
import fr.eni.ludotheque.bll.LocationService;
import fr.eni.ludotheque.bo.Exemplaire;
import fr.eni.ludotheque.bo.Location;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux locations.
 */
@Controller
@RequestMapping("/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final ExemplaireService exemplaireService;

    public LocationController(LocationService locationService, ExemplaireService exemplaireService) {
        this.locationService = locationService;
        this.exemplaireService = exemplaireService;
    }

    /**
     * Affiche la liste de toutes les locations.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping
    public String afficherListeLocations(Model model) {
        List<Location> locations = locationService.findAll();

        for (Location location : locations) {
            Exemplaire exemplaire = location.getExemplaire();
            if (exemplaire != null && exemplaire.getJeu() != null) {
                double tarifJournalier = exemplaire.getJeu().getTarifJournee();
                double prixTotal = locationService.calculerPrixTotal(location.getDateDebut(), location.getDateFin(), tarifJournalier);
                location.setPrixTotal(prixTotal);
            } else {
                location.setPrixTotal(0.0);
            }
        }

        model.addAttribute("locations", locations);
        model.addAttribute("body", "locations/liste");
        return "index";
    }

    /**
     * Affiche le formulaire pour ajouter une nouvelle location.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/ajouter")
    public String afficherFormulaireAjoutLocation(Model model) {
        model.addAttribute("location", new Location());
        model.addAttribute("exemplaires", exemplaireService.findAll());
        model.addAttribute("body", "locations/formulaire-location");
        return "index";
    }

    /**
     * Enregistre une nouvelle location ou met à jour une location existante.
     *
     * @param location      L'objet location soumis par le formulaire.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @param model         Le modèle pour passer les données à la vue.
     * @return La redirection vers la liste des locations si l'enregistrement réussit.
     */
    @PostMapping("/enregistrer")
    public String enregistrerLocation(Model model, @Valid Location location, BindingResult bindingResult) {
        logger.debug("Données reçues pour enregistrement : {}", location);

        // Charger les données nécessaires pour le formulaire en cas d'erreur
        model.addAttribute("exemplaires", exemplaireService.findAll());
        model.addAttribute("body", "locations/formulaire-location");

        // Validation des données du formulaire
        if (bindingResult.hasErrors()) {
            logger.warn("Erreurs de validation détectées : {}", bindingResult.getAllErrors());
            return "index";
        }

        // Vérification de l'exemplaire sélectionné
        if (location.getExemplaire() == null || location.getExemplaire().getId() == null) {
            logger.error("Aucun exemplaire sélectionné ou ID d'exemplaire manquant.");
            model.addAttribute("errorMessage", "Veuillez sélectionner un exemplaire valide.");
            return "index";
        }

        // Charger l'exemplaire depuis le service
        Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(location.getExemplaire().getId());
        if (exemplaireOpt.isEmpty()) {
            logger.error("Exemplaire introuvable pour l'ID : {}", location.getExemplaire().getId());
            model.addAttribute("errorMessage", "Exemplaire introuvable.");
            return "index";
        }

        Exemplaire exemplaireLoaded = exemplaireOpt.get();

        // Vérifier que l'exemplaire a un jeu et un tarif valide
        if (exemplaireLoaded.getJeu() == null || exemplaireLoaded.getJeu().getTarifJournee() == null) {
            logger.error("L'exemplaire chargé n'a pas de jeu valide ou de tarif défini.");
            model.addAttribute("errorMessage", "L'exemplaire sélectionné n'a pas de jeu valide ou de tarif défini.");
            return "index";
        }

        // Associer l'exemplaire chargé à la location
        location.setExemplaire(exemplaireLoaded);

        // Calculer le prix total de la location
        try {
            double tarifJournalier = exemplaireLoaded.getJeu().getTarifJournee();
            double prixTotal = locationService.calculerPrixTotal(location.getDateDebut(), location.getDateFin(), tarifJournalier);
            location.setPrixTotal(prixTotal);
            logger.debug("Prix total calculé : {}", prixTotal);
        } catch (Exception e) {
            logger.error("Erreur lors du calcul du prix total", e);
            model.addAttribute("errorMessage", "Erreur lors du calcul du prix total.");
            return "index";
        }

        // Sauvegarder la location
        try {
            locationService.saveOrUpdate(location);
            logger.info("Location enregistrée avec succès : {}", location);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la location", e);
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement de la location.");
            return "index";
        }

        // Rediriger vers la liste des locations
        return "redirect:/locations";
    }


    /**
     * Affiche le formulaire pour modifier une location existante.
     *
     * @param id    L'identifiant de la location à modifier.
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModificationLocation(@RequestParam("id") int id, Model model) {
        logger.debug("Chargement du formulaire de modification pour l'ID : {}", id);

        Optional<Location> locationOpt = locationService.findById(id);

        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            logger.debug("Location récupérée : {}", location);

            model.addAttribute("location", location);
            model.addAttribute("exemplaires", exemplaireService.findAll());
            model.addAttribute("body", "locations/formulaire-location");
            return "index";
        } else {
            logger.warn("Location introuvable pour l'ID : {}", id);
            model.addAttribute("errorMessage", "Location introuvable.");
            return "redirect:/locations";
        }
    }


    /**
     * Supprime une location existante.
     *
     * @param id L'identifiant de la location à supprimer.
     * @return La redirection vers la liste des locations après la suppression.
     */
    @GetMapping("/supprimer/{id}")
    public String supprimerLocationParId(@PathVariable("id") int id) {
        locationService.delete(id);
        return "redirect:/locations";
    }
}
