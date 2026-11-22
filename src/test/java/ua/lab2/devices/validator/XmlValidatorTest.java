package ua.lab2.devices.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ua.lab2.devices.TestResources;

class XmlValidatorTest {

    private static final Path XSD = TestResources.path("/xsd/devices.xsd");

    private final XmlValidator validator = new XmlValidator();

    @Test
    void projectXmlIsValid() throws XmlValidationException {
        ValidationResult result =
                validator.validate(TestResources.path("/xml/devices.xml"), XSD);

        assertTrue(result.isValid());
        assertTrue(result.errors().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/xml/bad-group.xml",
        "/xml/negative-price.xml",
        "/xml/duplicate-id.xml",
        "/xml/missing-id.xml",
        "/xml/invalid-syntax.xml"
    })
    void invalidDocumentsAreReportedWithErrors(String resource)
            throws XmlValidationException {
        ValidationResult result =
                validator.validate(TestResources.path(resource), XSD);

        assertFalse(result.isValid());
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void throwsWhenXmlFileIsMissing(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xml");

        assertThrows(XmlValidationException.class,
                () -> validator.validate(missing, XSD));
    }

    @Test
    void throwsWhenXsdFileIsMissing(@TempDir Path dir) {
        Path xml = TestResources.path("/xml/devices.xml");
        Path missing = dir.resolve("missing.xsd");

        assertThrows(XmlValidationException.class,
                () -> validator.validate(xml, missing));
    }

    @Test
    void throwsWhenPathIsNull() {
        assertThrows(XmlValidationException.class,
                () -> validator.validate(null, XSD));
    }

    @Test
    void throwsWhenSchemaIsBroken(@TempDir Path dir) throws IOException {
        Path brokenXsd = dir.resolve("broken.xsd");
        Files.writeString(brokenXsd, "this is not a schema");
        Path xml = TestResources.path("/xml/devices.xml");

        assertThrows(XmlValidationException.class,
                () -> validator.validate(xml, brokenXsd));
    }

    @Test
    void throwsWhenXmlPathIsDirectory(@TempDir Path dir) {
        assertThrows(XmlValidationException.class,
                () -> validator.validate(dir, XSD));
    }
}