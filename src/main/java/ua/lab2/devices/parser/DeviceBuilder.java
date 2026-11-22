package ua.lab2.devices.parser;

import java.math.BigDecimal;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.model.DeviceGroup;
import ua.lab2.devices.model.DeviceParams;
import ua.lab2.devices.model.Port;

/**
 * Assembles a {@link Device} from XML elements one by one.
 * Shared by all parsers so that conversion of text values to
 * Java types is written only once.
 */
public class DeviceBuilder {

    private Device device;
    private DeviceParams params;

    /**
     * Starts building a new device. Must be called before
     * {@link #setValue(String, String)}.
     *
     * @param id value of the {@code id} attribute
     */
    public void startDevice(String id) {
        device = new Device();
        device.setId(id);
        params = new DeviceParams();
    }

    /**
     * Applies value of an XML element to the device being built.
     * Elements that carry no value of their own (containers) and
     * unknown elements are ignored.
     *
     * @param tagName element name
     * @param text    text content of the element
     * @throws IllegalArgumentException if the text cannot be converted
     */
    public void setValue(String tagName, String text) {
        DeviceTag.fromName(tagName).ifPresent(tag -> apply(tag, text.trim()));
    }

    /**
     * Finishes the current device and resets the builder.
     *
     * @return completed device
     */
    public Device build() {
        device.setParams(params);
        Device result = device;
        device = null;
        params = null;
        return result;
    }

    private void apply(DeviceTag tag, String value) {
        switch (tag) {
            case NAME -> device.setName(value);
            case ORIGIN -> device.setOrigin(value);
            case PRICE -> device.setPrice(new BigDecimal(value));
            case PERIPHERAL -> params.setPeripheral(Boolean.parseBoolean(value));
            case ENERGY_CONSUMPTION ->
                    params.setEnergyConsumption(Integer.parseInt(value));
            case COOLER -> params.setCooler(Boolean.parseBoolean(value));
            case GROUP -> params.setGroup(DeviceGroup.fromXmlValue(value));
            case PORT -> params.addPort(Port.fromXmlValue(value));
            case CRITICAL -> device.setCritical(Boolean.parseBoolean(value));
            default -> {
                // container elements have no value of their own
            }
        }
    }
}