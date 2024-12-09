package fr.eni.ludotheque.bo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class Exemplaire {

    private Integer id;

    @NotEmpty(message = "Le code barre est obligatoire.")
    @Size(min = 3, max = 13, message = "Le code barre doit faire entre 3 et 13 caractères.")
    private String codeBarre;

    private boolean louable;

    @NotNull(message = "L'exemplaire doit être associé à un jeu. Veuillez sélectionner un jeu.")
    private Jeu jeu;

    // Constructeurs
    public Exemplaire() {
    }

    public Exemplaire(Integer id, String codeBarre, boolean louable, Jeu jeu) {
        this.id = id;
        this.codeBarre = codeBarre;
        this.louable = louable;
        this.jeu = jeu;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public boolean isLouable() {
        return louable;
    }

    public void setLouable(boolean louable) {
        this.louable = louable;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    // hashCode et equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exemplaire that = (Exemplaire) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ExemplaireJeu{" +
                "id=" + id +
                ", codeBarre='" + codeBarre + '\'' +
                ", louable=" + louable +
                ", jeu=" + jeu +
                '}';
    }
}
