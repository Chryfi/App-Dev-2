package main.java.de.appdev2.exceptions;

/**
 * Eine checked Exception die bei Datenbank Fehlern geworfen wird.
 */
public class DataBaseException extends Exception {
    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException() {
        super();
    }
}
