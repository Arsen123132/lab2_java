package ua.lab2.devices.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PortTest {

    @ParameterizedTest
    @EnumSource(Port.class)
    void fromXmlValueRestoresEveryConstant(Port port) {
        assertEquals(port, Port.fromXmlValue(port.getXmlValue()));
    }

    @Test
    void fromXmlValueRejectsUnknownValue() {
        assertThrows(IllegalArgumentException.class,
                () -> Port.fromXmlValue("HDMI"));
    }

    @Test
    void fromXmlValueRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Port.fromXmlValue(null));
    }
}