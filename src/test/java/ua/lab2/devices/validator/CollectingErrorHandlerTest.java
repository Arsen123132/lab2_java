package ua.lab2.devices.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

class CollectingErrorHandlerTest {

    private static SAXParseException exception(String message) {
        return new SAXParseException(message, null, null, 3, 7);
    }

    @Test
    void recordsWarningsErrorsAndFatalErrors() {
        CollectingErrorHandler handler = new CollectingErrorHandler();

        handler.warning(exception("warning"));
        handler.error(exception("error"));
        handler.fatalError(exception("fatal"));

        assertEquals(3, handler.getErrors().size());
    }

    @Test
    void messageContainsPositionAndText() {
        CollectingErrorHandler handler = new CollectingErrorHandler();

        handler.error(exception("bad value"));

        assertEquals("line 3, column 7: bad value", handler.getErrors().get(0));
    }

    @Test
    void startsEmpty() {
        assertTrue(new CollectingErrorHandler().getErrors().isEmpty());
    }
}