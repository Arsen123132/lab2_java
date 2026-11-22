package ua.lab2.devices.model;

/**
 * Group of a computer component.
 * Constants correspond to the {@code GroupType} enumeration in devices.xsd.
 */
public enum DeviceGroup {

    INPUT_OUTPUT("input-output"),
    MULTIMEDIA("multimedia");

    private final String xmlValue;

    DeviceGroup(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    /**
     * Returns the textual value used in the XML document.
     *
     * @return value as written in XML
     */
    public String getXmlValue() {
        return xmlValue;
    }

    /**
     * Finds a group by its XML representation.
     *
     * @param value text taken from the XML document
     * @return matching group
     * @throws IllegalArgumentException if no group matches the value
     */
    public static DeviceGroup fromXmlValue(String value) {
        for (DeviceGroup group : values()) {
            if (group.xmlValue.equals(value)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown device group: " + value);
    }
}