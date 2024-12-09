package fr.eni.ludotheque.bo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;

public class Location {

    private Integer id;

    @NotNull(message = "L'exemplaire est obligatoire.")
    private Exemplaire exemplaire;

    @NotNull(message = "La date de début est obligatoire.")
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé.")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire.")
    private LocalDate dateFin;

    private boolean retour;

    @Positive(message = "Le prix total doit être un nombre positif.")
    private Double prixTotal;

    // Constructeurs
    public Location() {
    }

    public Location(Integer id, Exemplaire exemplaire, LocalDate dateDebut, LocalDate dateFin, boolean retour, Double prixTotal) {
        this.id = id;
        this.exemplaire = exemplaire;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.retour = retour;
        this.prixTotal = prixTotal;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isRetour() {
        return retour;
    }

    public void setRetour(boolean retour) {
        this.retour = retour;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    // Méthodes utilitaires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", exemplaire=" + exemplaire +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", retour=" + retour +
                ", prixTotal=" + prixTotal +
                '}';
    }
}
