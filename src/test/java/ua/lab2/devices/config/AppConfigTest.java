package ua.lab2.devices.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class AppConfigTest {

    @Test
    void loadsAllPathsFromResource() throws IOException {
        AppConfig config = AppConfig.load("test-app.properties");

        assertEquals(Path.of("src/main/resources/xml/devices.xml"), config.xmlPath());
        assertEquals(Path.of("src/main/resources/xsd/devices.xsd"), config.xsdPath());
        assertEquals(Path.of("src/main/resources/xsl/devices-by-group.xsl"),
                config.xslPath());
        assertEquals(Path.of("target/test-output/devices-by-group.xml"),
                config.outputPath());
    }

    @Test
    void throwsWhenResourceIsMissing() {
        assertThrows(FileNotFoundException.class,
                () -> AppConfig.load("missing.properties"));
    }

    @Test
    void throwsWhenPropertyIsMissing() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> AppConfig.load("config-missing-key.properties"));

        assertTrue(e.getMessage().contains("xsd.path"));
    }

    @Test
    void throwsWhenPropertyIsBlank() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> AppConfig.load("config-blank-value.properties"));

        assertTrue(e.getMessage().contains("xml.path"));
    }
}