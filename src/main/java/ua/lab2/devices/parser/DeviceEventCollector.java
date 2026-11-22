package ua.lab2.devices.parser;

import java.util.ArrayList;
import java.util.List;
import ua.lab2.devices.model.Device;

/**
 * Collects devices from a stream of XML events. Shared by the event-based
 * parsers (SAX and StAX), so the logic is written only once.
 */
class DeviceEventCollector {

    private final DeviceBuilder builder = new DeviceBuilder();
    private final List<Device> devices = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();

    /**
     * Handles the start of an element.
     *
     * @param name element name
     * @param id   value of the {@code id} attribute, may be {@code null}
     */
    void startElement(String name, String id) {
        text.setLength(0);
        if (DeviceTag.DEVICE.matches(name)) {
            builder.startDevice(id);
        }
    }

    /**
     * Handles a piece of text inside the current element.
     *
     * @param chars text chunk, parsers may deliver text in several chunks
     */
    void characters(String chars) {
        text.append(chars);
    }

    /**
     * Handles the end of an element.
     *
     * @param name element name
     */
    void endElement(String name) {
        builder.setValue(name, text.toString());
        if (DeviceTag.DEVICE.matches(name)) {
            devices.add(builder.build());
        }
    }

    List<Device> getDevices() {
        return devices;
    }
}