package ua.lab2.devices.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DeviceTest {

    private static Device createDevice() {
        DeviceParams params = new DeviceParams();
        params.setGroup(DeviceGroup.MULTIMEDIA);
        params.addPort(Port.USB);

        Device device = new Device();
        device.setId("dev-001");
        device.setName("Webcam");
        device.setOrigin("Switzerland");
        device.setPrice(new BigDecimal("3299.00"));
        device.setParams(params);
        device.setCritical(false);
        return device;
    }

    @Test
    void gettersReturnValuesSetBefore() {
        Device device = createDevice();

        assertEquals("dev-001", device.getId());
        assertEquals("Webcam", device.getName());
        assertEquals("Switzerland", device.getOrigin());
        assertEquals(new BigDecimal("3299.00"), device.getPrice());
        assertEquals(DeviceGroup.MULTIMEDIA, device.getParams().getGroup());
        assertFalse(device.isCritical());
    }

    @Test
    void equalsAndHashCodeDependOnAllFields() {
        Device first = createDevice();
        Device second = createDevice();

        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        second.setId("dev-002");
        assertNotEquals(first, second);

        second = createDevice();
        second.setName("Other");
        assertNotEquals(first, second);

        second = createDevice();
        second.setOrigin("China");
        assertNotEquals(first, second);

        second = createDevice();
        second.setPrice(BigDecimal.ONE);
        assertNotEquals(first, second);

        second = createDevice();
        second.setParams(new DeviceParams());
        assertNotEquals(first, second);

        second = createDevice();
        second.setCritical(true);
        assertNotEquals(first, second);
    }

        @Test
    void equalsReturnsFalseForNullAndOtherClass() {
        Device device = createDevice();

        // equals() must be called on the tested object itself
        assertFalse(device.equals(null));
        assertFalse(device.equals("device"));
    }

    @Test
    void toStringContainsKeyFields() {
        String text = createDevice().toString();

        assertTrue(text.contains("dev-001"));
        assertTrue(text.contains("Webcam"));
        assertTrue(text.contains("3299.00"));
    }

}