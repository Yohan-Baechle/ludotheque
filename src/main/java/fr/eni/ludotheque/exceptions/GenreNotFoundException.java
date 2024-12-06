package fr.eni.ludotheque.exceptions;

/**
 * Exception levée lorsqu'un genre n'est pas trouvé dans la base de données.
 */
public class GenreNotFoundException extends RuntimeException {

    // Constructeur sans message
    public GenreNotFoundException() {
        super();
    }

    // Constructeur avec un message
    public GenreNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec un message et une cause
    public GenreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public GenreNotFoundException(Throwable cause) {
        super(cause);
    }
}
