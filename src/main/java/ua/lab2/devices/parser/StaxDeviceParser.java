package ua.lab2.devices.parser;

import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ua.lab2.devices.model.Device;

/**
 * Parser that pulls events from the document on demand (StAX).
 */
public class StaxDeviceParser extends AbstractDeviceParser {

    @Override
    protected List<Device> doParse(InputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);

        DeviceEventCollector collector = new DeviceEventCollector();
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT ->
                            collector.startElement(reader.getLocalName(),
                                    reader.getAttributeValue(null,
                                            DeviceTag.ID_ATTRIBUTE));
                    case XMLStreamConstants.CHARACTERS ->
                            collector.characters(reader.getText());
                    case XMLStreamConstants.END_ELEMENT ->
                            collector.endElement(reader.getLocalName());
                    default -> {
                        // other events (comments, document end) are not needed
                    }
                }
            }
        } finally {
            reader.close();
        }
        return collector.getDevices();
    }
}