package main.java.de.appdev2.exceptions;

/**
 * Eine checked Exception die bei falschen Eingaben geworfen wird.
 */
public class IllegalInputException extends Exception {
    public IllegalInputException(String message) {
        super(message);
    }

    public IllegalInputException() {
    }
}
