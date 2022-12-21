package main.java.de.appdev2.exceptions;

public class DataBaseException extends Exception {
    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException() {
        super();
    }
}
