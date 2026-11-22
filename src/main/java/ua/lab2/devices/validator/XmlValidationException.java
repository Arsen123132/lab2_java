package ua.lab2.devices.validator;

/**
 * Signals that validation could not be performed (missing file,
 * broken schema, I/O problem). An invalid document is not an error
 * of this kind, see {@link ValidationResult}.
 */
public class XmlValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public XmlValidationException(String message) {
        super(message);
    }

    public XmlValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}