package ua.lab2.devices.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ValidationResultTest {

    @Test
    void resultWithoutErrorsIsValid() {
        assertTrue(new ValidationResult(List.of()).isValid());
    }

    @Test
    void resultWithErrorsIsNotValid() {
        assertFalse(new ValidationResult(List.of("error")).isValid());
    }

    @Test
    void errorsAreCopiedAndUnmodifiable() {
        List<String> source = new ArrayList<>(List.of("error"));
        ValidationResult result = new ValidationResult(source);
        source.add("another");

        assertTrue(result.errors().size() == 1);
        assertThrows(UnsupportedOperationException.class,
                () -> result.errors().add("x"));
    }
}