package fr.eni.ludotheque.bo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Jeu {

	private Integer id;

	@NotBlank(message = "Le titre est obligatoire.")
	@Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères.")
	private String titre;

	@NotBlank(message = "La référence est obligatoire.")
	@Size(min = 3, max = 50, message = "La référence doit contenir entre 3 et 50 caractères.")
	private String reference;

	@NotBlank(message = "La description est obligatoire.")
	@Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.")
	private String description;

	@NotNull(message = "Le tarif par journée est obligatoire.")
	@Positive(message = "Le tarif par journée doit être un nombre positif.")
	private Double tarifJournee;

	@NotNull(message = "L''âge minimum est obligatoire.")
	@Positive(message = "L'âge minimum doit être un nombre positif.")
	private Integer ageMin;

	@NotNull(message = "La durée est obligatoire.")
	@Positive(message = "La durée doit être un nombre positif.")
	private Integer duree;

	private List<Genre> genres;

	// Constructeurs
	public Jeu() {
		// Constructeur par défaut
	}

	public Jeu(String titre, String reference, String description, Double tarifJournee, Integer ageMin, Integer duree) {
		this.titre = titre;
		this.reference = reference;
		this.description = description;
		this.tarifJournee = tarifJournee;
		this.ageMin = ageMin;
		this.duree = duree;
	}

	public Jeu(Integer id, String titre, String reference, String description, Double tarifJournee, Integer ageMin,
			Integer duree, Set<Genre> genres) {
		this.id = id;
		this.titre = titre;
		this.reference = reference;
		this.description = description;
		this.tarifJournee = tarifJournee;
		this.ageMin = ageMin;
		this.duree = duree;
		for(Genre genre :genres) {
			this.genres.add(genre);
		}
	}

	// Getters et setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getTarifJournee() {
		return tarifJournee;
	}

	public void setTarifJournee(Double tarifJournee) {
		this.tarifJournee = tarifJournee;
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getDuree() {
		return duree;
	}

	public void setDuree(Integer duree) {
		this.duree = duree;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	// toString
	@Override
	public String toString() {
		return "Jeu [id=" + id + ", titre=" + titre + ", reference=" + reference + ", description=" + description
				+ ", tarifJournee=" + tarifJournee + ", ageMin=" + ageMin + ", duree=" + duree + ", genres=" + genres
				+ "]";
	}

	// hashCode et equals
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jeu other = (Jeu) obj;
		return Objects.equals(id, other.id);
	}
}
