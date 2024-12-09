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

import fr.eni.ludotheque.bll.ExemplaireService;
import fr.eni.ludotheque.bll.JeuService;
import fr.eni.ludotheque.bo.Exemplaire;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux exemplaires de jeux.
 */
@Controller
@RequestMapping("/exemplaires")
public class ExemplaireController {

	private final ExemplaireService exemplaireService;
	private final JeuService jeuService;

	public ExemplaireController(ExemplaireService exemplaireService, JeuService jeuService) {
		this.exemplaireService = exemplaireService;
		this.jeuService = jeuService;
	}

	/**
	 * Affiche la liste de tous les exemplaires de jeux.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping
	public String afficherListeExemplaires(Model model) {
		List<Exemplaire> exemplaires = exemplaireService.findAll();
		model.addAttribute("exemplaires", exemplaires);
		model.addAttribute("body", "exemplaires/liste");
		return "index";
	}

	/**
	 * Affiche le formulaire pour ajouter un nouvel exemplaire.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping("/ajouter")
	public String afficherFormulaireAjoutExemplaire(Model model) {
		model.addAttribute("exemplaire", new Exemplaire());
		model.addAttribute("jeux", jeuService.findAll());
		model.addAttribute("body", "exemplaires/formulaire-exemplaire");
		return "index";
	}

	/**
	 * Enregistre un nouvel exemplaire ou met à jour un exemplaire existant.
	 *
	 * @param model         Le modèle pour passer les données à la vue.
	 * @param exemplaire    L'objet exemplaire soumis par le formulaire.
	 * @param bindingResult Le résultat de la validation du formulaire.
	 * @return La redirection vers la liste des exemplaires si l'enregistrement
	 *         réussit, ou le formulaire si des erreurs sont présentes.
	 */
	@PostMapping("/enregistrer")
	public String enregistrerExemplaire(Model model, @Valid Exemplaire exemplaire, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("exemplaire", exemplaire);
			model.addAttribute("jeux", jeuService.findAll());
			model.addAttribute("body", "exemplaires/formulaire-exemplaire");
			return "index";
		}

		exemplaireService.saveOrUpdate(exemplaire);
		return "redirect:/exemplaires";
	}

	/**
	 * Affiche le formulaire pour modifier un exemplaire existant.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @param id    L'identifiant de l'exemplaire à modifier.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping("/modifier")
	public String afficherFormulaireModificationExemplaire(Model model, @RequestParam("id") int id) {
		Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(id);
		if (exemplaireOpt.isPresent()) {
			model.addAttribute("exemplaire", exemplaireOpt.get());
			model.addAttribute("jeux", jeuService.findAll());
			model.addAttribute("body", "exemplaires/formulaire-exemplaire");
			return "index";
		} else {
			model.addAttribute("errorMessage", "Exemplaire introuvable.");
			model.addAttribute("body", "exemplaires/liste");
			return "index";
		}
	}

	/**
	 * Supprime un exemplaire existant.
	 *
	 * @param id L'identifiant de l'exemplaire à supprimer.
	 * @return La redirection vers la liste des exemplaires après la suppression.
	 */
	@GetMapping("/supprimer/{id}")
	public String supprimerExemplaireParId(@PathVariable("id") int id) {
		exemplaireService.delete(id);
		return "redirect:/exemplaires";
	}
}
