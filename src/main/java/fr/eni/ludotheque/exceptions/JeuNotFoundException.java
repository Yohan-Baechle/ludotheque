package fr.eni.ludotheque.exceptions;

/**
 * Exception levée lorsqu'un jeu n'est pas trouvé dans la base de données.
 */
public class JeuNotFoundException extends RuntimeException {

    // Constructeur sans message
    public JeuNotFoundException() {
        super();
    }

    // Constructeur avec un message
    public JeuNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec un message et une cause
    public JeuNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public JeuNotFoundException(Throwable cause) {
        super(cause);
    }
}
