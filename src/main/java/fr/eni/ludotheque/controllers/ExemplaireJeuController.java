package fr.eni.ludotheque.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.ludotheque.bo.ExemplaireJeu;
import fr.eni.ludotheque.services.ExemplaireJeuService;
import fr.eni.ludotheque.services.JeuService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/exemplaires")
public class ExemplaireJeuController {

    private final ExemplaireJeuService exemplaireJeuService;
    private final JeuService jeuService;

    public ExemplaireJeuController(ExemplaireJeuService exemplaireJeuService, JeuService jeuService) {
        this.exemplaireJeuService = exemplaireJeuService;
        this.jeuService = jeuService;
    }

    /*
     * Afficher la liste des exemplaires
     */
    @GetMapping
    public String exemplaires(Model model) {
        List<ExemplaireJeu> exemplaires = exemplaireJeuService.findAll();
        model.addAttribute("exemplaires", exemplaires);
        model.addAttribute("body", "exemplaires/liste");
        return "index";
    }

    /*
     * Afficher le formulaire pour ajouter un exemplaire
     */
    @GetMapping("/ajouter")
    public String pageAjouterExemplaire(Model model) {
        model.addAttribute("exemplaire", new ExemplaireJeu());
        model.addAttribute("jeux", jeuService.findAll()); // Ajouter les jeux
        model.addAttribute("body", "exemplaires/formulaire-exemplaire");
        return "index";
    }

    /*
     * Enregistrer un nouvel exemplaire
     */
    @PostMapping("/enregistrer")
    public String ajouterOuModifierExemplaire(Model model, @Valid ExemplaireJeu exemplaire, BindingResult bindingResult) {
        model.addAttribute("body", "exemplaires/formulaire-exemplaire");
        if (bindingResult.hasErrors()) {
            model.addAttribute("jeux", jeuService.findAll()); // Réinjecte les jeux en cas d'erreur
            return "index";
        }

        exemplaireJeuService.save(exemplaire);
        return "redirect:/exemplaires";
    }

    /*
     * Afficher le formulaire pour modifier un exemplaire
     */
    @GetMapping("/modifier")
    public String getModifierExemplaire(Model model, @RequestParam("id") int id) {
        Optional<ExemplaireJeu> exemplaireOpt = exemplaireJeuService.findById(id);
        if (exemplaireOpt.isPresent()) {
            model.addAttribute("exemplaire", exemplaireOpt.get());
            model.addAttribute("jeux", jeuService.findAll()); // Ajouter les jeux
            model.addAttribute("body", "exemplaires/formulaire-exemplaire");
        } else {
            model.addAttribute("errorMessage", "Exemplaire introuvable.");
            model.addAttribute("body", "exemplaires/liste");
        }
        return "index";
    }

    /*
     * Supprimer un exemplaire
     */
    @GetMapping("/supprimer/{id}")
    public String supprimerExemplaire(@PathVariable("id") int id) {
        exemplaireJeuService.delete(id);
        return "redirect:/exemplaires";
    }
}
