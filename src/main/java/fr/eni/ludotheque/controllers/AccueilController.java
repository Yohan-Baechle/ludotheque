package fr.eni.ludotheque.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour gérer la page d'accueil.
 */
@Controller
public class AccueilController {

    /**
     * Affiche la page d'accueil.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom du fichier fragment utilisé comme contenu dynamique.
     */
    @GetMapping("/")
    public String afficherAccueil(Model model) {
        model.addAttribute("body", "accueil");
        model.addAttribute("message", "Bienvenue sur le tableau de bord de la Ludothèque !");
        return "index";
    }
}
