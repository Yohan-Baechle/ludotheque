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

import fr.eni.ludotheque.bll.JeuService;
import fr.eni.ludotheque.bo.Jeu;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux jeux.
 */
@Controller
@RequestMapping("/jeux")
public class JeuController {

    private final JeuService jeuService;

    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    /**
     * Affiche la liste de tous les jeux.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping
    public String afficherListeJeux(Model model) {
        List<Jeu> jeux = jeuService.findAll();
        model.addAttribute("jeux", jeux);
        model.addAttribute("body", "jeux/liste");
        return "index";
    }

    /**
     * Affiche le formulaire pour ajouter un nouveau jeu.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/ajouter")
    public String afficherFormulaireAjoutJeu(Model model) {
        model.addAttribute("jeu", new Jeu());
        model.addAttribute("body", "jeux/formulaire-jeu");
        return "index";
    }

    /**
     * Enregistre un nouveau jeu ou modifie un jeu existant.
     *
     * @param model         Le modèle pour passer les données à la vue.
     * @param jeu           L'objet jeu soumis par le formulaire.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return La redirection vers la liste des jeux si l'enregistrement réussit,
     *         ou le formulaire si des erreurs sont présentes.
     */
    @PostMapping("/enregistrer")
    public String enregistrerJeu(Model model, @Valid Jeu jeu, BindingResult bindingResult) {
        model.addAttribute("body", "jeux/formulaire-jeu");
        if (bindingResult.hasErrors()) {
            return "index";
        }
        jeuService.saveOrUpdate(jeu);
        return "redirect:/jeux";
    }

    /**
     * Affiche le formulaire de modification d'un jeu existant.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @param id    L'identifiant du jeu à modifier.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModificationJeu(Model model, @RequestParam("id") int id) {
        Optional<Jeu> jeuOpt = jeuService.findById(id);
        if (jeuOpt.isPresent()) {
            model.addAttribute("jeu", jeuOpt.get());
            model.addAttribute("body", "jeux/formulaire-jeu");
        } else {
            model.addAttribute("errorMessage", "Jeu introuvable.");
            model.addAttribute("body", "jeux/liste");
        }
        return "index";
    }

    /**
     * Supprime un jeu existant.
     *
     * @param id L'identifiant du jeu à supprimer.
     * @return La redirection vers la liste des jeux après la suppression.
     */
    @GetMapping("/supprimer/{id}")
    public String supprimerJeuParId(@PathVariable("id") int id) {
        jeuService.delete(id);
        return "redirect:/jeux";
    }
}
