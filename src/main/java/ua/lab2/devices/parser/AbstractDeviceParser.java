package ua.lab2.devices.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import ua.lab2.devices.model.Device;

/**
 * Common part of all parsers: checks the file, logs the progress and
 * converts technical exceptions to {@link DeviceParsingException}.
 * Subclasses only implement the parsing technique itself.
 */
public abstract class AbstractDeviceParser implements DeviceParser {

    private final Logger logger = LogManager.getLogger(getClass());

    @Override
    public List<Device> parse(Path xmlPath) throws DeviceParsingException {
        logger.info("Parsing {}", xmlPath);
        if (xmlPath == null || Files.notExists(xmlPath)) {
            logger.error("XML file not found: {}", xmlPath);
            throw new DeviceParsingException("XML file not found: " + xmlPath);
        }
        try (InputStream in = Files.newInputStream(xmlPath)) {
            List<Device> devices = doParse(in);
            logger.info("Parsed {} devices", devices.size());
            return devices;
        } catch (IOException | SAXException | ParserConfigurationException
                 | XMLStreamException | IllegalArgumentException e) {
            logger.error("Failed to parse {}", xmlPath, e);
            throw new DeviceParsingException("Failed to parse " + xmlPath, e);
        }
    }

    /**
     * Reads devices from the opened stream.
     *
     * @param in stream with XML content, closed by the caller
     * @return devices in document order
     */
    protected abstract List<Device> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException,
            XMLStreamException;
}