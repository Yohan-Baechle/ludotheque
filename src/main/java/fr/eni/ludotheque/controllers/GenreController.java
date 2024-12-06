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

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.services.GenreService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/genres")
public class GenreController {

	private final GenreService genreService;

	public GenreController(GenreService genreService) {
		this.genreService = genreService;
	}

	/*
	 * Afficher la liste des genres
	 */
	@GetMapping
	public String genres(Model model) {
		List<Genre> genres = genreService.findAll();
		model.addAttribute("genres", genres);
		model.addAttribute("body", "genres/liste");
		return "index";
	}

	/*
	 * Afficher le formulaire pour ajouter un genre
	 */
	@GetMapping("/ajouter")
	public String pageAjouterGenre(Model model) {
		model.addAttribute("genre", new Genre());
		model.addAttribute("body", "genres/formulaire-genre");
		return "index";
	}

	/*
	 * Enregistrer un nouveau genre
	 */
	@PostMapping("/enregistrer")
	public String ajouterGenre(Model model, @Valid Genre genre, BindingResult bindingResult) {
		model.addAttribute("body", "genres/formulaire-genre");
		if (bindingResult.hasErrors()) {
			return "index";
		}
		genreService.save(genre);
		return "redirect:/genres";
	}

	/*
	 * Afficher le formulaire pour modifier un genre
	 */
	@GetMapping("/modifier")
	public String getModifierGenre(Model model, @RequestParam("id") int id) {
		Optional<Genre> genreOpt = genreService.findById(id);
		if (genreOpt.isPresent()) {
			model.addAttribute("genre", genreOpt.get());
			model.addAttribute("body", "genres/formulaire-genre");
		} else {
			// Gestion de l'erreur : genre non trouvé
			model.addAttribute("errorMessage", "Genre introuvable.");
			model.addAttribute("body", "genres/genres");
		}
		return "index";
	}

	/*
	 * Supprimer un genre
	 */
	@GetMapping("/supprimer/{id}")
	public String supprimerGenre(@PathVariable("id") int id) {
		genreService.delete(id);
		return "redirect:/genres";
	}
}
