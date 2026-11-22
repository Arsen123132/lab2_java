package ua.lab2.devices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.lab2.devices.config.AppConfig;
import ua.lab2.devices.model.Device;
import ua.lab2.devices.parser.DeviceParser;
import ua.lab2.devices.parser.DeviceParsingException;
import ua.lab2.devices.transform.XslTransformer;
import ua.lab2.devices.validator.ValidationResult;
import ua.lab2.devices.validator.XmlValidator;

@ExtendWith(MockitoExtension.class)
class DeviceApplicationTest {

    private static final Path XML = Path.of("devices.xml");
    private static final Path XSD = Path.of("devices.xsd");
    private static final Path XSL = Path.of("devices.xsl");
    private static final Path OUT = Path.of("out.xml");

    @Mock
    private DeviceParser domParser;
    @Mock
    private DeviceParser saxParser;
    @Mock
    private XmlValidator validator;
    @Mock
    private XslTransformer transformer;

    private DeviceApplication application;

    private static Device device(String name, String price) {
        Device device = new Device();
        device.setName(name);
        device.setOrigin("Ukraine");
        device.setPrice(new BigDecimal(price));
        return device;
    }

    @BeforeEach
    void setUp() {
        Map<String, DeviceParser> parsers = new LinkedHashMap<>();
        parsers.put("DOM", domParser);
        parsers.put("SAX", saxParser);
        application = new DeviceApplication(
                new AppConfig(XML, XSD, XSL, OUT), parsers, validator, transformer);
    }

    @Test
    void loadSortedOrdersDevicesByPrice() throws Exception {
        when(domParser.parse(XML)).thenReturn(
                List.of(device("Expensive", "900"), device("Cheap", "100")));

        List<String> names = application.loadSorted(domParser).stream()
                .map(Device::getName).toList();

        assertEquals(List.of("Cheap", "Expensive"), names);
    }

    @Test
    void runsAllStepsForValidDocument() throws Exception {
        when(validator.validate(XML, XSD)).thenReturn(new ValidationResult(List.of()));
        when(domParser.parse(XML)).thenReturn(List.of(device("A", "1")));
        when(saxParser.parse(XML)).thenReturn(List.of(device("B", "2")));

        assertTrue(application.run());

        verify(domParser).parse(XML);
        verify(saxParser).parse(XML);
        verify(transformer).transform(XML, XSL, OUT);
    }

    @Test
    void stopsWhenDocumentIsInvalid() throws Exception {
        when(validator.validate(XML, XSD))
                .thenReturn(new ValidationResult(List.of("some error")));

        assertFalse(application.run());

        verifyNoInteractions(domParser, saxParser, transformer);
    }

    @Test
    void propagatesParserFailureAndSkipsTransformation() throws Exception {
        when(validator.validate(XML, XSD)).thenReturn(new ValidationResult(List.of()));
        when(domParser.parse(XML)).thenThrow(new DeviceParsingException("broken"));

        assertThrows(DeviceParsingException.class, () -> application.run());

        verifyNoInteractions(saxParser, transformer);
    }
}