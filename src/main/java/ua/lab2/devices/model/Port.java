package ua.lab2.devices.model;

/**
 * Connection port of a computer component.
 * Constants correspond to the {@code PortType} enumeration in devices.xsd.
 */
public enum Port {

    COM,
    USB,
    LPT;

    /**
     * Returns the textual value used in the XML document.
     *
     * @return value as written in XML
     */
    public String getXmlValue() {
        return name();
    }

    /**
     * Finds a port by its XML representation.
     *
     * @param value text taken from the XML document
     * @return matching port
     * @throws IllegalArgumentException if no port matches the value
     */
    public static Port fromXmlValue(String value) {
        for (Port port : values()) {
            if (port.name().equals(value)) {
                return port;
            }
        }
        throw new IllegalArgumentException("Unknown port: " + value);
    }
}