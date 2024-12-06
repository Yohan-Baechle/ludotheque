package fr.eni.ludotheque.bo;

import java.util.Objects;

public class ExemplaireJeu {

    private Integer id;
    private String codeBarre;
    private boolean louable;
    private Jeu jeu;

    // Constructeurs
    public ExemplaireJeu() {
    }

    public ExemplaireJeu(Integer id, String codeBarre, boolean louable, Jeu jeu) {
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

    // Méthodes de comparaison et affichage
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExemplaireJeu that = (ExemplaireJeu) o;
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
