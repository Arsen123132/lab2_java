package ua.lab2.devices.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import ua.lab2.devices.TestResources;

class XslTransformerTest {

    private static final Path XML = TestResources.path("/xml/devices.xml");
    private static final Path XSL = TestResources.path("/xsl/devices-by-group.xsl");

    private final XslTransformer transformer = new XslTransformer();

    private Document result;

    @BeforeEach
    void transformProjectXml(@TempDir Path dir) throws Exception {
        Path output = dir.resolve("grouped.xml");
        transformer.transform(XML, XSL, output);
        result = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(output.toFile());
    }

    private String xpath(String expression) throws Exception {
        return XPathFactory.newInstance().newXPath().evaluate(expression, result);
    }

    @Test
    void createsGroupsInAlphabeticalOrder() throws Exception {
        assertEquals("GroupedDevices", result.getDocumentElement().getNodeName());
        assertEquals("2", xpath("count(/GroupedDevices/Group)"));
        assertEquals("input-output", xpath("/GroupedDevices/Group[1]/@name"));
        assertEquals("multimedia", xpath("/GroupedDevices/Group[2]/@name"));
    }

    @Test
    void writesNumberOfDevicesInEachGroup() throws Exception {
        assertEquals("3", xpath("/GroupedDevices/Group[@name='input-output']/@count"));
        assertEquals("3", xpath("/GroupedDevices/Group[@name='multimedia']/@count"));
    }

    @Test
    void sortsDevicesByNameInsideGroup() throws Exception {
        assertEquals("Logitech MX Keys",
                xpath("/GroupedDevices/Group[1]/Device[1]/Name"));
        assertEquals("StartTech Multi I/O Card",
                xpath("/GroupedDevices/Group[1]/Device[3]/Name"));
        assertEquals("ASUS GeForce RTX 4060",
                xpath("/GroupedDevices/Group[2]/Device[1]/Name"));
        assertEquals("Sound BlasterX G6",
                xpath("/GroupedDevices/Group[2]/Device[3]/Name"));
    }

    @Test
    void keepsAllDevicesWithTheirData() throws Exception {
        assertEquals("6", xpath("count(//Device)"));
        assertEquals("dev-001", xpath("/GroupedDevices/Group[1]/Device[1]/@id"));
        assertEquals("3499.00", xpath("/GroupedDevices/Group[1]/Device[1]/Price"));
        assertEquals("true", xpath("/GroupedDevices/Group[1]/Device[1]/Critical"));
        assertEquals("3", xpath("count(/GroupedDevices/Group[1]/Device[3]/Type/Ports/Port)"));
    }

    @Test
    void doesNotAddPortsToDevicesWithoutThem() throws Exception {
        assertEquals("0", xpath("count(/GroupedDevices/Group[2]/Device[1]/Type/Ports)"));
    }

    @Test
    void removesGroupElementFromType() throws Exception {
        assertEquals("0", xpath("count(//Device/Type/Group)"));
    }

    @Test
    void createsMissingParentDirectories(@TempDir Path dir) throws Exception {
        Path output = dir.resolve("nested/deeper/grouped.xml");

        transformer.transform(XML, XSL, output);

        assertTrue(Files.exists(output));
    }

    @Test
    void throwsWhenXmlFileIsMissing(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xml");
        Path output = dir.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(missing, XSL, output));
    }

    @Test
    void throwsWhenXslFileIsMissing(@TempDir Path dir) {
        Path missing = dir.resolve("missing.xsl");
        Path output = dir.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(XML, missing, output));
    }

    @Test
    void throwsWhenInputPathIsNull(@TempDir Path dir) {
        Path output = dir.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(null, XSL, output));
    }

    @Test
    void throwsWhenOutputPathIsNull() {
        assertThrows(TransformationException.class,
                () -> transformer.transform(XML, XSL, null));
    }

    @Test
    void throwsWhenStylesheetIsBroken(@TempDir Path dir) throws IOException {
        Path brokenXsl = dir.resolve("broken.xsl");
        Files.writeString(brokenXsl, "this is not a stylesheet");
        Path output = dir.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(XML, brokenXsl, output));
    }

    @Test
    void throwsWhenXmlIsMalformed(@TempDir Path dir) {
        Path malformed = TestResources.path("/xml/invalid-syntax.xml");
        Path output = dir.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(malformed, XSL, output));
    }

    @Test
    void throwsWhenOutputDirectoryCannotBeCreated(@TempDir Path dir)
            throws IOException {
        Path file = Files.writeString(dir.resolve("file.txt"), "text");
        Path output = file.resolve("out.xml");

        assertThrows(TransformationException.class,
                () -> transformer.transform(XML, XSL, output));
    }
}