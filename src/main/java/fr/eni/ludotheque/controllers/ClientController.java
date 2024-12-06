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

import fr.eni.ludotheque.bo.Client;
import fr.eni.ludotheque.services.ClientService;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux clients.
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	/**
	 * Affiche la liste de tous les clients.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping
	public String afficherListeClients(Model model) {
		List<Client> clients = clientService.findAll();
		model.addAttribute("clients", clients);
		model.addAttribute("body", "clients/liste");
		return "index";
	}

	/**
	 * Affiche le formulaire d'ajout d'un nouveau client.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping("/ajouter")
	public String afficherFormulaireAjoutClient(Model model) {
		model.addAttribute("client", new Client());
		model.addAttribute("body", "clients/formulaire-client");
		return "index";
	}

	/**
	 * Enregistre un nouveau client ou met à jour un client existant.
	 *
	 * @param model         Le modèle pour passer les données à la vue.
	 * @param client        L'objet client soumis par le formulaire.
	 * @param bindingResult Le résultat de la validation du formulaire.
	 * @return La redirection vers la liste des clients si l'enregistrement réussit,
	 *         ou le formulaire si des erreurs sont présentes.
	 */
	@PostMapping("/enregistrer")
	public String enregistrerClient(Model model, @Valid Client client, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("body", "clients/formulaire-client");
			return "index";
		}
		clientService.saveOrUpdate(client);
		return "redirect:/clients";
	}

	/**
	 * Affiche le formulaire de modification d'un client existant.
	 *
	 * @param model Le modèle pour passer les données à la vue.
	 * @param id    L'identifiant du client à modifier.
	 * @return Le nom de la vue à afficher.
	 */
	@GetMapping("/modifier")
	public String afficherFormulaireModificationClient(Model model, @RequestParam("id") int id) {
		Optional<Client> clientOpt = clientService.findById(id);
		if (clientOpt.isPresent()) {
			model.addAttribute("client", clientOpt.get());
			model.addAttribute("body", "clients/formulaire-client");
			return "index";
		} else {
			model.addAttribute("errorMessage", "Client introuvable.");
			model.addAttribute("body", "clients/liste");
			return "index";
		}
	}

	/**
	 * Supprime un client existant.
	 *
	 * @param id L'identifiant du client à supprimer.
	 * @return La redirection vers la liste des clients après la suppression.
	 */
	@GetMapping("/supprimer/{id}")
	public String supprimerClientParId(@PathVariable("id") int id) {
		clientService.delete(id);
		return "redirect:/clients";
	}
}
