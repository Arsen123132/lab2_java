package ua.lab2.devices.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Initialization parameters of the application.
 *
 * @param xmlPath    source XML document
 * @param xsdPath    schema used for validation
 * @param xslPath    stylesheet used for transformation
 * @param outputPath file where the transformation result is written
 */
public record AppConfig(Path xmlPath, Path xsdPath, Path xslPath, Path outputPath) {

    /**
     * Loads the configuration from a properties file on the classpath.
     *
     * @param resourceName name of the properties resource
     * @return loaded configuration
     * @throws IOException              if the resource is missing or unreadable
     * @throws IllegalArgumentException if a required property is missing or blank
     */
    public static AppConfig load(String resourceName) throws IOException {
        Properties properties = new Properties();
        try (InputStream in =
                     AppConfig.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new FileNotFoundException(
                        "Configuration not found: " + resourceName);
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        return new AppConfig(
                Path.of(require(properties, "xml.path")),
                Path.of(require(properties, "xsd.path")),
                Path.of(require(properties, "xsl.path")),
                Path.of(require(properties, "output.path")));
    }

    private static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing property: " + key);
        }
        return value.trim();
    }
}