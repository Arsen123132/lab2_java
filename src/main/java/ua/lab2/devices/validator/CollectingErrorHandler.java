package ua.lab2.devices.validator;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Error handler that remembers every problem instead of stopping
 * at the first one.
 */
class CollectingErrorHandler implements ErrorHandler {

    private final List<String> errors = new ArrayList<>();

    @Override
    public void warning(SAXParseException e) {
        record(e);
    }

    @Override
    public void error(SAXParseException e) {
        record(e);
    }

    @Override
    public void fatalError(SAXParseException e) {
        record(e);
    }

    List<String> getErrors() {
        return errors;
    }

    private void record(SAXParseException e) {
        errors.add("line " + e.getLineNumber() + ", column "
                + e.getColumnNumber() + ": " + e.getMessage());
    }
}