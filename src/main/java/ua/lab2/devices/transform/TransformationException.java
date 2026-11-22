package ua.lab2.devices.transform;

/**
 * Signals that an XSL transformation could not be performed.
 */
public class TransformationException extends Exception {

    private static final long serialVersionUID = 1L;

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }
}