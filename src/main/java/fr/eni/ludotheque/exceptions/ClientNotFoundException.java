package fr.eni.ludotheque.exceptions;

/**
 * Exception levée lorsqu'un client n'est pas trouvé dans la base de données.
 */
public class ClientNotFoundException extends RuntimeException {

    // Constructeur sans message
    public ClientNotFoundException() {
        super();
    }

    // Constructeur avec un message
    public ClientNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec un message et une cause
    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public ClientNotFoundException(Throwable cause) {
        super(cause);
    }
}
