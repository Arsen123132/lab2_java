package ua.lab2.devices.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DeviceGroupTest {

    @ParameterizedTest
    @EnumSource(DeviceGroup.class)
    void fromXmlValueRestoresEveryConstant(DeviceGroup group) {
        assertEquals(group, DeviceGroup.fromXmlValue(group.getXmlValue()));
    }

    @Test
    void xmlValuesMatchSchema() {
        assertEquals("input-output", DeviceGroup.INPUT_OUTPUT.getXmlValue());
        assertEquals("multimedia", DeviceGroup.MULTIMEDIA.getXmlValue());
    }

    @Test
    void fromXmlValueRejectsUnknownValue() {
        assertThrows(IllegalArgumentException.class,
                () -> DeviceGroup.fromXmlValue("unknown"));
    }

    @Test
    void fromXmlValueRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DeviceGroup.fromXmlValue(null));
    }
}