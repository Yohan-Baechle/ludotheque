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

import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.services.JeuService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/jeux")
public class JeuController {

	private final JeuService jeuService;

	public JeuController(JeuService jeuService) {
		this.jeuService = jeuService;
	}

	/*
	 * Afficher la liste des jeux
	 */
	@GetMapping
	public String jeux(Model model) {
		List<Jeu> jeux = jeuService.findAll();
		model.addAttribute("jeux", jeux);
		model.addAttribute("body", "jeux/liste");
		return "index";
	}

	/*
	 * Afficher le formulaire pour ajouter un jeu
	 */
	@GetMapping("/ajouter")
	public String pageAjouterJeu(Model model) {
		model.addAttribute("jeu", new Jeu());
		model.addAttribute("body", "jeux/formulaire-jeu");
		return "index";
	}

	/*
	 * Enregistrer un nouveau jeu ou modifier un jeu existant
	 */
	@PostMapping("/enregistrer")
	public String ajouterOuModifierJeu(Model model, @Valid Jeu jeu, BindingResult bindingResult) {
		model.addAttribute("body", "jeux/formulaire-jeu");
		if (bindingResult.hasErrors()) {
			return "index";
		}
		jeuService.save(jeu);
		return "redirect:/jeux";
	}

	/*
	 * Afficher le formulaire pour modifier un jeu
	 */
	@GetMapping("/modifier")
	public String getModifierJeu(Model model, @RequestParam("id") int id) {
		Optional<Jeu> jeuOpt = jeuService.findById(id);
		if (jeuOpt.isPresent()) {
			model.addAttribute("jeu", jeuOpt.get());
			model.addAttribute("body", "jeux/formulaire-jeu");
		} else {
			// Gestion de l'erreur : jeu non trouvé
			model.addAttribute("errorMessage", "Jeu introuvable.");
			model.addAttribute("body", "jeux/liste");
		}
		return "index";
	}

	/*
	 * Supprimer un jeu
	 */
	@GetMapping("/supprimer/{id}")
	public String supprimerJeu(@PathVariable("id") int id) {
		jeuService.delete(id);
		return "redirect:/jeux";
	}
}
