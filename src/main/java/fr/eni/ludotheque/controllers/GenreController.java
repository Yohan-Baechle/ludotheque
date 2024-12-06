package fr.eni.ludotheque.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.services.GenreService;
import fr.eni.ludotheque.exceptions.GenreNotFoundException;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux genres.
 */
@Controller
@RequestMapping("/genres")
public class GenreController {

    private static final Logger logger = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    /**
     * Affiche la liste de tous les genres.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping
    public String afficherListeGenres(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("body", "genres/liste");
        return "index";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau genre.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/ajouter")
    public String afficherFormulaireAjoutGenre(Model model) {
        model.addAttribute("genre", new Genre());
        model.addAttribute("body", "genres/formulaire-genre");
        return "index";
    }

    /**
     * Enregistre un nouveau genre ou met à jour un genre existant.
     *
     * @param model         Le modèle pour passer les données à la vue.
     * @param genre         L'objet genre soumis par le formulaire.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return La redirection vers la liste des genres si l'enregistrement réussit,
     *         ou le formulaire si des erreurs sont présentes.
     */
    @PostMapping("/enregistrer")
    public String enregistrerGenre(Model model, @Valid Genre genre, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("body", "genres/formulaire-genre");
            return "index";
        }
        genreService.saveOrUpdate(genre);
        logger.info("Genre créé ou mis à jour : {}", genre);
        return "redirect:/genres";
    }

    /**
     * Affiche le formulaire de modification d'un genre existant.
     *
     * @param model Le modèle pour passer les données à la vue.
     * @param id    L'identifiant du genre à modifier.
     * @return Le nom de la vue à afficher.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModificationGenre(Model model, @RequestParam("id") int id) {
        Optional<Genre> genreOpt = genreService.findById(id);
        if (genreOpt.isPresent()) {
            model.addAttribute("genre", genreOpt.get());
            model.addAttribute("body", "genres/formulaire-genre");
            return "index";
        } else {
            model.addAttribute("errorMessage", "Genre introuvable.");
            model.addAttribute("body", "genres/liste");
            logger.error("Genre non trouvé, ID : {}", id);
            return "index";
        }
    }

    /**
     * Supprime un genre existant.
     *
     * @param id L'identifiant du genre à supprimer.
     * @return La redirection vers la liste des genres après la suppression.
     */
    @GetMapping("/supprimer/{id}")
    public String supprimerGenreParId(@PathVariable("id") int id) {
        try {
            genreService.delete(id);
            logger.info("Genre supprimé, ID : {}", id);
        } catch (GenreNotFoundException e) {
            logger.error("Erreur lors de la suppression du genre, ID : {}", id, e);
        }
        return "redirect:/genres";
    }
}
