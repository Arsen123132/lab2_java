package ua.lab2.devices;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.lab2.devices.config.AppConfig;
import ua.lab2.devices.parser.DeviceParser;
import ua.lab2.devices.parser.DeviceParsingException;
import ua.lab2.devices.parser.DomDeviceParser;
import ua.lab2.devices.parser.SaxDeviceParser;
import ua.lab2.devices.parser.StaxDeviceParser;
import ua.lab2.devices.transform.TransformationException;
import ua.lab2.devices.transform.XslTransformer;
import ua.lab2.devices.validator.XmlValidationException;
import ua.lab2.devices.validator.XmlValidator;

/**
 * Entry point of the application.
 */
public final class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String DEFAULT_CONFIG = "app.properties";

    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args optional name of a properties resource, {@code app.properties}
     *             is used by default
     * @throws IllegalStateException if the scenario did not complete, which
     *                               makes the process exit with a non-zero code
     */
    public static void main(String[] args) {
        String configResource = args.length > 0 ? args[0] : DEFAULT_CONFIG;
        if (!run(configResource)) {
            throw new IllegalStateException("Application finished with errors");
        }
    }

    private static boolean run(String configResource) {
        try {
            AppConfig config = AppConfig.load(configResource);

            Map<String, DeviceParser> parsers = new LinkedHashMap<>();
            parsers.put("DOM", new DomDeviceParser());
            parsers.put("SAX", new SaxDeviceParser());
            parsers.put("StAX", new StaxDeviceParser());

            return new DeviceApplication(config, parsers,
                    new XmlValidator(), new XslTransformer()).run();
        } catch (IOException | IllegalArgumentException | DeviceParsingException
                 | XmlValidationException | TransformationException e) {
            LOGGER.error("Application failed", e);
            return false;
        }
    }
}