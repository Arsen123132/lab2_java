package ua.lab2.devices.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.model.DeviceGroup;
import ua.lab2.devices.model.Port;

class DeviceBuilderTest {

    private DeviceBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new DeviceBuilder();
        builder.startDevice("dev-007");
    }

    @Test
    void buildsDeviceFromAllValues() {
        builder.setValue("Name", "Monitor");
        builder.setValue("Origin", "Poland");
        builder.setValue("Price", "8999.90");
        builder.setValue("Peripheral", "true");
        builder.setValue("EnergyConsumption", "40");
        builder.setValue("Cooler", "false");
        builder.setValue("Group", "multimedia");
        builder.setValue("Port", "USB");
        builder.setValue("Port", "COM");
        builder.setValue("Critical", "true");

        Device device = builder.build();

        assertEquals("dev-007", device.getId());
        assertEquals("Monitor", device.getName());
        assertEquals("Poland", device.getOrigin());
        assertEquals(new BigDecimal("8999.90"), device.getPrice());
        assertTrue(device.getParams().isPeripheral());
        assertEquals(40, device.getParams().getEnergyConsumption());
        assertFalse(device.getParams().hasCooler());
        assertEquals(DeviceGroup.MULTIMEDIA, device.getParams().getGroup());
        assertEquals(List.of(Port.USB, Port.COM), device.getParams().getPorts());
        assertTrue(device.isCritical());
    }

    @Test
    void trimsWhitespaceAroundValues() {
        builder.setValue("Name", "  Monitor \n");

        assertEquals("Monitor", builder.build().getName());
    }

    @Test
    void ignoresContainerAndUnknownElements() {
        builder.setValue("Devices", "text");
        builder.setValue("Type", "text");
        builder.setValue("Unknown", "text");

        Device device = builder.build();

        assertNull(device.getName());
        assertNull(device.getPrice());
    }

    @Test
    void rejectsValueOutsideEnumeration() {
        assertThrows(IllegalArgumentException.class,
                () -> builder.setValue("Group", "unknown"));
    }

    @Test
    void rejectsNonNumericPrice() {
        assertThrows(NumberFormatException.class,
                () -> builder.setValue("Price", "abc"));
    }

    @Test
    void buildResetsStateForNextDevice() {
        builder.setValue("Port", "USB");
        Device first = builder.build();

        builder.startDevice("dev-008");
        Device second = builder.build();

        assertNotSame(first.getParams(), second.getParams());
        assertTrue(second.getParams().getPorts().isEmpty());
        assertEquals(1, first.getParams().getPorts().size());
    }
}