package ua.lab2.devices.validator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Validates XML documents against an XSD schema.
 */
public class XmlValidator {

    private static final Logger LOGGER = LogManager.getLogger(XmlValidator.class);

    /**
     * Validates the document and collects all problems found.
     *
     * @param xmlPath path to the XML document
     * @param xsdPath path to the XSD schema
     * @return result with the list of errors, empty for a valid document
     * @throws XmlValidationException if a file is missing or unreadable,
     *                                or the schema itself is broken
     */
    public ValidationResult validate(Path xmlPath, Path xsdPath)
            throws XmlValidationException {
        requireExisting(xmlPath, "XML file");
        requireExisting(xsdPath, "XSD file");
        LOGGER.info("Validating {} against {}", xmlPath, xsdPath);

        Schema schema = loadSchema(xsdPath);
        CollectingErrorHandler handler = new CollectingErrorHandler();
        Validator validator = schema.newValidator();
        validator.setErrorHandler(handler);

        try (InputStream in = Files.newInputStream(xmlPath)) {
            validator.validate(new StreamSource(in));
        } catch (SAXException e) {
            // fatal errors (e.g. malformed XML) are already recorded by the handler
            LOGGER.debug("Validation stopped by fatal error", e);
        } catch (IOException e) {
            throw new XmlValidationException("Cannot read " + xmlPath, e);
        }

        ValidationResult result = new ValidationResult(handler.getErrors());
        if (result.isValid()) {
            LOGGER.info("{} is valid", xmlPath);
        } else {
            result.errors().forEach(error -> LOGGER.warn("Invalid XML: {}", error));
        }
        return result;
    }

    private Schema loadSchema(Path xsdPath) throws XmlValidationException {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return factory.newSchema(new StreamSource(xsdPath.toFile()));
        } catch (SAXException e) {
            LOGGER.error("Cannot load schema {}", xsdPath, e);
            throw new XmlValidationException("Cannot load schema " + xsdPath, e);
        }
    }

    private void requireExisting(Path path, String description)
            throws XmlValidationException {
        if (path == null || Files.notExists(path)) {
            LOGGER.error("{} not found: {}", description, path);
            throw new XmlValidationException(description + " not found: " + path);
        }
    }
}