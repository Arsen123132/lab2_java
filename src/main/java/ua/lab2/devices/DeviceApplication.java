package ua.lab2.devices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.lab2.devices.comparator.DeviceComparator;
import ua.lab2.devices.config.AppConfig;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.parser.DeviceParser;
import ua.lab2.devices.parser.DeviceParsingException;
import ua.lab2.devices.transform.TransformationException;
import ua.lab2.devices.transform.XslTransformer;
import ua.lab2.devices.validator.ValidationResult;
import ua.lab2.devices.validator.XmlValidationException;
import ua.lab2.devices.validator.XmlValidator;

/**
 * Runs the whole scenario: validation, parsing with every available parser,
 * sorting and XSL transformation.
 */
public class DeviceApplication {

    private static final Logger LOGGER = LogManager.getLogger(DeviceApplication.class);

    private final AppConfig config;
    private final Map<String, DeviceParser> parsers;
    private final XmlValidator validator;
    private final XslTransformer transformer;

    /**
     * Creates the application.
     *
     * @param config      paths of the files to work with
     * @param parsers     parsers by display name, used in the given order
     * @param validator   XSD validator
     * @param transformer XSL transformer
     */
    public DeviceApplication(AppConfig config, Map<String, DeviceParser> parsers,
            XmlValidator validator, XslTransformer transformer) {
        this.config = config;
        this.parsers = new LinkedHashMap<>(parsers);
        this.validator = validator;
        this.transformer = transformer;
    }

    /**
     * Executes the scenario.
     *
     * @return {@code false} if the XML document is invalid and nothing
     *         else was done, {@code true} if everything completed
     * @throws DeviceParsingException  if a parser fails
     * @throws XmlValidationException  if validation could not be performed
     * @throws TransformationException if the transformation fails
     */
    public boolean run() throws DeviceParsingException, XmlValidationException,
            TransformationException {
        ValidationResult result = validator.validate(config.xmlPath(), config.xsdPath());
        if (!result.isValid()) {
            LOGGER.error("XML is invalid, {} problem(s) found", result.errors().size());
            return false;
        }

        for (Map.Entry<String, DeviceParser> entry : parsers.entrySet()) {
            List<Device> devices = loadSorted(entry.getValue());
            LOGGER.info("{} parser read {} devices, sorted by price:",
                    entry.getKey(), devices.size());
            devices.forEach(device -> LOGGER.info("  {} ({}) - {}",
                    device.getName(), device.getOrigin(), device.getPrice()));
        }

        transformer.transform(config.xmlPath(), config.xslPath(), config.outputPath());
        return true;
    }

    /**
     * Reads devices with the given parser and sorts them by price.
     * The list returned by the parser is not modified.
     */
    List<Device> loadSorted(DeviceParser parser) throws DeviceParsingException {
        List<Device> devices = new ArrayList<>(parser.parse(config.xmlPath()));
        devices.sort(DeviceComparator.BY_PRICE);
        return devices;
    }
}