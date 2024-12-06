package fr.eni.ludotheque.bo;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Genre {

	private Integer id;

	@NotBlank(message = "Le libellé est obligatoire.")
	@Size(min = 3, max = 50, message = "Le libellé doit contenir entre 3 et 50 caractères.")
	private String libelle;

	// Constructeurs
	public Genre() {
		// Constructeur par défaut
	}

	public Genre(Integer id, String libelle) {
		this.id = id;
		this.libelle = libelle;
	}

	// Getters et setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	// toString
	@Override
	public String toString() {
		return "Genre [id=" + id + ", libelle=" + libelle + "]";
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
		Genre other = (Genre) obj;
		return Objects.equals(id, other.id);
	}
}
