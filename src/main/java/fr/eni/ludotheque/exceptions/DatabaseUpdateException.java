package fr.eni.ludotheque.exceptions;

/**
 * Exception levée lors d'une erreur dans une opération de mise à jour de la base de données.
 */
public class DatabaseUpdateException extends RuntimeException {

    // Constructeur sans message
    public DatabaseUpdateException() {
        super();
    }

    // Constructeur avec un message
    public DatabaseUpdateException(String message) {
        super(message);
    }

    // Constructeur avec un message et une cause
    public DatabaseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public DatabaseUpdateException(Throwable cause) {
        super(cause);
    }
}
