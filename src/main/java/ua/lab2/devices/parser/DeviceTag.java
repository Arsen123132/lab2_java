package ua.lab2.devices.parser;

import java.util.Optional;

/**
 * Names of XML elements and attributes used in the devices document.
 */
public enum DeviceTag {

    DEVICES("Devices"),
    DEVICE("Device"),
    NAME("Name"),
    ORIGIN("Origin"),
    PRICE("Price"),
    TYPE("Type"),
    PERIPHERAL("Peripheral"),
    ENERGY_CONSUMPTION("EnergyConsumption"),
    COOLER("Cooler"),
    GROUP("Group"),
    PORTS("Ports"),
    PORT("Port"),
    CRITICAL("Critical");

    /** Name of the attribute that holds the device identifier. */
    public static final String ID_ATTRIBUTE = "id";

    private final String xmlName;

    DeviceTag(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getXmlName() {
        return xmlName;
    }

    /**
     * Checks whether the given element name is the name of this tag.
     *
     * @param name element name taken from the XML document
     * @return {@code true} if names are equal
     */
    public boolean matches(String name) {
        return xmlName.equals(name);
    }

    /**
     * Finds a tag by its XML name.
     *
     * @param name element name taken from the XML document
     * @return matching tag or empty value if the name is unknown
     */
    public static Optional<DeviceTag> fromName(String name) {
        for (DeviceTag tag : values()) {
            if (tag.matches(name)) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }
}