package ua.lab2.devices.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DeviceTagTest {

    @ParameterizedTest
    @EnumSource(DeviceTag.class)
    void fromNameFindsEveryTag(DeviceTag tag) {
        assertEquals(tag, DeviceTag.fromName(tag.getXmlName()).orElseThrow());
    }

    @Test
    void fromNameReturnsEmptyForUnknownName() {
        assertTrue(DeviceTag.fromName("Unknown").isEmpty());
    }

    @Test
    void matchesComparesXmlNames() {
        assertTrue(DeviceTag.DEVICE.matches("Device"));
        assertFalse(DeviceTag.DEVICE.matches("Devices"));
    }
}