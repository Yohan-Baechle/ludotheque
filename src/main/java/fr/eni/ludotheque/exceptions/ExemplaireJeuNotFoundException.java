package fr.eni.ludotheque.exceptions;

/**
 * Exception lancée lorsqu'un exemplaire de jeu n'est pas trouvé.
 */
public class ExemplaireJeuNotFoundException extends RuntimeException {

    /**
     * Constructeur par défaut.
     */
    public ExemplaireJeuNotFoundException() {
        super("Exemplaire de jeu introuvable.");
    }

    /**
     * Constructeur avec un message personnalisé.
     *
     * @param message Le message d'erreur.
     */
    public ExemplaireJeuNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message personnalisé et une cause.
     *
     * @param message Le message d'erreur.
     * @param cause La cause de l'exception.
     */
    public ExemplaireJeuNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur avec une cause.
     *
     * @param cause La cause de l'exception.
     */
    public ExemplaireJeuNotFoundException(Throwable cause) {
        super(cause);
    }
}
