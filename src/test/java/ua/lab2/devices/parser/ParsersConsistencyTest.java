package ua.lab2.devices.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab2.devices.TestResources;
import ua.lab2.devices.model.Device;

class ParsersConsistencyTest {

    @Test
    void allParsersReturnEqualResults() throws DeviceParsingException {
        Path path = TestResources.path("/xml/devices.xml");

        List<Device> dom = new DomDeviceParser().parse(path);

        assertEquals(dom, new SaxDeviceParser().parse(path));
        assertEquals(dom, new StaxDeviceParser().parse(path));
    }
}