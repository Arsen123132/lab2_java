package ua.lab2.devices.transform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.OutputStream;

/**
 * Converts an XML file to another XML structure using an XSL stylesheet.
 */
public class XslTransformer {

    private static final Logger LOGGER = LogManager.getLogger(XslTransformer.class);

    /**
     * Applies the stylesheet to the XML file and writes the result.
     * Missing parent directories of the output file are created.
     *
     * @param xmlPath    path to the source XML file
     * @param xslPath    path to the XSL stylesheet
     * @param outputPath path of the file to create
     * @throws TransformationException if a file is missing or unwritable,
     *                                 or the stylesheet or XML is invalid
     */
    public void transform(Path xmlPath, Path xslPath, Path outputPath)
            throws TransformationException {
        requireExisting(xmlPath, "XML file");
        requireExisting(xslPath, "XSL file");
        if (outputPath == null) {
            throw new TransformationException("Output path is not specified");
        }
        LOGGER.info("Transforming {} with {} into {}", xmlPath, xslPath, outputPath);

        try {
            Files.createDirectories(outputPath.toAbsolutePath().getParent());

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer =
                    factory.newTransformer(new StreamSource(xslPath.toFile()));

            // the stream is closed even if the transformation fails
            try (OutputStream out = Files.newOutputStream(outputPath)) {
                transformer.transform(new StreamSource(xmlPath.toFile()),
                        new StreamResult(out));
            }
        } catch (TransformerException | IOException e) {
            LOGGER.error("Transformation failed", e);
            throw new TransformationException("Transformation failed", e);
        }
        LOGGER.info("Transformation finished: {}", outputPath);
    }

    private void requireExisting(Path path, String description)
            throws TransformationException {
        if (path == null || Files.notExists(path)) {
            LOGGER.error("{} not found: {}", description, path);
            throw new TransformationException(description + " not found: " + path);
        }
    }
}