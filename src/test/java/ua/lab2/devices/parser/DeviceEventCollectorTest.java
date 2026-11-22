package ua.lab2.devices.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab2.devices.model.Device;

class DeviceEventCollectorTest {

    @Test
    void collectsDeviceFromEvents() {
        DeviceEventCollector collector = new DeviceEventCollector();

        collector.startElement("Devices", null);
        collector.startElement("Device", "dev-001");
        collector.startElement("Name", null);
        collector.characters("Mo");
        collector.characters("use");
        collector.endElement("Name");
        collector.endElement("Device");
        collector.endElement("Devices");

        List<Device> devices = collector.getDevices();
        assertEquals(1, devices.size());
        assertEquals("dev-001", devices.get(0).getId());
        assertEquals("Mouse", devices.get(0).getName());
    }

    @Test
    void whitespaceBetweenElementsDoesNotLeakIntoValues() {
        DeviceEventCollector collector = new DeviceEventCollector();

        collector.startElement("Device", "dev-002");
        collector.characters("\n    ");
        collector.startElement("Origin", null);
        collector.characters("China");
        collector.endElement("Origin");
        collector.characters("\n");
        collector.endElement("Device");

        assertEquals("China", collector.getDevices().get(0).getOrigin());
    }

    @Test
    void startsEmptyWhenNoEventsArrived() {
        assertTrue(new DeviceEventCollector().getDevices().isEmpty());
    }
}