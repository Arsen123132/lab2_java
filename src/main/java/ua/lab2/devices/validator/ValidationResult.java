package ua.lab2.devices.validator;

import java.util.List;

/**
 * Outcome of validating an XML document against a schema.
 *
 * @param errors descriptions of all found problems, empty if the document
 *               is valid
 */
public record ValidationResult(List<String> errors) {

    public ValidationResult {
        errors = List.copyOf(errors);
    }

    /**
     * Tells whether the document conforms to the schema.
     *
     * @return {@code true} if no errors were found
     */
    public boolean isValid() {
        return errors.isEmpty();
    }
}