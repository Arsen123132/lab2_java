package ua.lab2.devices.parser;

/**
 * Signals that an XML document could not be read or has wrong content.
 */
public class DeviceParsingException extends Exception {

    private static final long serialVersionUID = 1L;

    public DeviceParsingException(String message) {
        super(message);
    }

    public DeviceParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}