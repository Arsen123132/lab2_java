package ua.lab2.devices.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.lab2.devices.TestResources;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.model.DeviceGroup;
import ua.lab2.devices.model.DeviceParams;
import ua.lab2.devices.model.Port;

/**
 * Tests every parser must pass. Concrete parsers only supply an instance.
 */
abstract class DeviceParserContractTest {

    private static final String DEVICES_XML = "/xml/devices.xml";

    private DeviceParser parser;

    protected abstract DeviceParser createParser();

    @BeforeEach
    void setUp() {
        parser = createParser();
    }

    @Test
    void parsesAllDevicesInDocumentOrder() throws DeviceParsingException {
        List<Device> devices = parser.parse(TestResources.path(DEVICES_XML));

        assertEquals(6, devices.size());
        assertEquals("dev-001", devices.get(0).getId());
        assertEquals("dev-006", devices.get(5).getId());
    }

    @Test
    void parsesAllFieldsOfFirstDevice() throws DeviceParsingException {
        Device actual = parser.parse(TestResources.path(DEVICES_XML)).get(0);

        DeviceParams params = new DeviceParams();
        params.setPeripheral(true);
        params.setEnergyConsumption(5);
        params.setCooler(false);
        params.setGroup(DeviceGroup.INPUT_OUTPUT);
        params.addPort(Port.USB);

        Device expected = new Device();
        expected.setId("dev-001");
        expected.setName("Logitech MX Keys");
        expected.setOrigin("Switzerland");
        expected.setPrice(new BigDecimal("3499.00"));
        expected.setParams(params);
        expected.setCritical(true);

        assertEquals(expected, actual);
    }

    @Test
    void deviceWithoutPortsHasEmptyPortList() throws DeviceParsingException {
        Device gpu = parser.parse(TestResources.path(DEVICES_XML)).get(2);

        assertEquals("dev-003", gpu.getId());
        assertTrue(gpu.getParams().hasCooler());
        assertTrue(gpu.getParams().getPorts().isEmpty());
    }

    @Test
    void keepsOrderOfSeveralPorts() throws DeviceParsingException {
        Device card = parser.parse(TestResources.path(DEVICES_XML)).get(5);

        assertEquals(List.of(Port.COM, Port.USB, Port.LPT),
                card.getParams().getPorts());
    }

    @Test
    void throwsWhenFileDoesNotExist(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xml");

        assertThrows(DeviceParsingException.class, () -> parser.parse(missing));
    }

    @Test
    void throwsWhenPathIsNull() {
        assertThrows(DeviceParsingException.class, () -> parser.parse(null));
    }

    @Test
    void throwsWhenPathIsDirectory(@TempDir Path dir) {
        assertThrows(DeviceParsingException.class, () -> parser.parse(dir));
    }

    @Test
    void throwsOnMalformedXml() {
        Path path = TestResources.path("/xml/invalid-syntax.xml");

        assertThrows(DeviceParsingException.class, () -> parser.parse(path));
    }

    @Test
    void throwsOnValueOutsideEnumeration() {
        Path path = TestResources.path("/xml/bad-group.xml");

        assertThrows(DeviceParsingException.class, () -> parser.parse(path));
    }
}