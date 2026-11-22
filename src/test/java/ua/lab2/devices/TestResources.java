package ua.lab2.devices;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Helper for locating files from the classpath in tests.
 */
public final class TestResources {

    private TestResources() {
    }

    /**
     * Returns a classpath resource as a file path.
     *
     * @param name resource name starting with a slash, e.g. {@code /xml/a.xml}
     * @return path to the resource
     */
    public static Path path(String name) {
        try {
            return Path.of(Objects.requireNonNull(
                    TestResources.class.getResource(name), name).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}