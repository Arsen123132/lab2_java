package ua.lab2.devices;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MainTest {

    private static final Path TEST_OUTPUT =
            Path.of("target/test-output/devices-by-group.xml");

    @Test
    void completesFullScenarioWithGivenConfig() throws IOException {
        Files.deleteIfExists(TEST_OUTPUT);

        Main.main(new String[] {"test-app.properties"});

        assertTrue(Files.exists(TEST_OUTPUT));
    }

    @Test
    void usesDefaultConfigWhenNoArgumentsGiven() {
        assertDoesNotThrow(() -> Main.main(new String[0]));
    }

    @Test
    void failsWhenConfigIsMissing() {
        assertThrows(IllegalStateException.class,
                () -> Main.main(new String[] {"missing.properties"}));
    }

    @Test
    void failsWhenConfigIsIncomplete() {
        assertThrows(IllegalStateException.class,
                () -> Main.main(new String[] {"config-missing-key.properties"}));
    }

    @Test
    void failsWhenXmlIsInvalid() {
        assertThrows(IllegalStateException.class,
                () -> Main.main(new String[] {"test-app-invalid.properties"}));
    }
}