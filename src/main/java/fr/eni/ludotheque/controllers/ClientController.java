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

@Controller
@RequestMapping("/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	/*
	 * Afficher la liste des clients
	 */
	@GetMapping
	public String clients(Model model) {
		List<Client> clients = clientService.findAll();
		model.addAttribute("clients", clients);
		model.addAttribute("body", "clients/liste");
		return "index";
	}

	/*
	 * Afficher le formulaire pour ajouter un client
	 */
	@GetMapping("/ajouter")
	public String pageAjouterClient(Model model) {
		model.addAttribute("client", new Client());
		model.addAttribute("body", "clients/formulaire-client");
		return "index";
	}

	/*
	 * Enregistrer un nouveau client
	 */
	@PostMapping("/enregistrer")
	public String ajouterClient(Model model, @Valid Client client, BindingResult bindingResult) {
		model.addAttribute("body", "clients/formulaire-client");
		if (bindingResult.hasErrors()) {
			return "index";
		}
		clientService.save(client);
		return "redirect:/clients";
	}

	/*
	 * Afficher le formulaire pour modifier un client
	 */
	@GetMapping("/modifier")
	public String getModifierClient(Model model, @RequestParam("id") int id) {
		Optional<Client> clientOpt = clientService.findById(id);
		if (clientOpt.isPresent()) {
			model.addAttribute("client", clientOpt.get());
			model.addAttribute("body", "clients/formulaire-client");
		} else {
			// Gestion de l'erreur : client non trouvé
			model.addAttribute("errorMessage", "Client introuvable.");
			model.addAttribute("body", "clients/clients");
		}
		return "index";
	}

	/*
	 * Supprimer un client
	 */
	@GetMapping("/supprimer/{id}")
	public String supprimerClient(@PathVariable("id") int id) {
		clientService.delete(id);
		return "redirect:/clients";
	}
}
