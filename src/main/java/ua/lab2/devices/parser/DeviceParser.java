package ua.lab2.devices.parser;

import java.nio.file.Path;
import java.util.List;
import ua.lab2.devices.model.Device;

/**
 * Reads computer components from an XML file.
 */
public interface DeviceParser {

    /**
     * Parses the XML file into a list of devices.
     *
     * @param xmlPath path to the XML file
     * @return devices in document order
     * @throws DeviceParsingException if the file is missing, malformed
     *                                or contains invalid values
     */
    List<Device> parse(Path xmlPath) throws DeviceParsingException;
}