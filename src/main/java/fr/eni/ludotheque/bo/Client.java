package fr.eni.ludotheque.bo;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Client {

	private Integer id;

	@NotBlank(message = "Le nom est obligatoire.")
	@Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères.")
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$", message = "Le nom ne doit contenir que des lettres, des espaces et des tirets.")
	private String nom;

	@NotBlank(message = "Le prénom est obligatoire.")
	@Size(min = 3, max = 50, message = "Le prénom doit contenir entre 3 et 50 caractères.")
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$", message = "Le prénom ne doit contenir que des lettres, des espaces et des tirets.")
	private String prenom;

	@NotBlank(message = "L''email est obligatoire.")
	@Email(message = "L'email doit être valide.")
	@Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères.")
	private String email;

	@Pattern(regexp = "^$|^\\+?[0-9]*$", message = "Le numéro de téléphone doit être valide.")
	private String noTelephone;

	@NotBlank(message = "La rue est obligatoire.")
	@Size(max = 100, message = "La rue ne doit pas dépasser 100 caractères.")
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s]+$", message = "La rue ne doit contenir que des lettres, des chiffres, des espaces et des tirets.")
	private String rue;

	@NotBlank(message = "Le code postal est obligatoire.")
	@Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit être composé de 5 chiffres.")
	private String codePostal;

	@NotBlank(message = "La ville est obligatoire.")
	@Size(max = 50, message = "La ville ne doit pas dépasser 50 caractères.")
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$", message = "La ville ne doit contenir que des lettres, des espaces et des tirets.")
	private String ville;

	// Constructeurs
	public Client() {
		// Constructeur par défaut
	}

	public Client(String nom, String prenom, String email, String rue, String codePostal, String ville) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}

	public Client(Integer id, String nom, String prenom, String email, String noTelephone, String rue,
			String codePostal, String ville) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.noTelephone = noTelephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}

	// Getters et setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNoTelephone() {
		return noTelephone;
	}

	public void setNoTelephone(String noTelephone) {
		this.noTelephone = noTelephone;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	// toString
	@Override
	public String toString() {
		return "Client [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", noTelephone="
				+ noTelephone + ", rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + "]";
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
		Client other = (Client) obj;
		return Objects.equals(id, other.id);
	}
}