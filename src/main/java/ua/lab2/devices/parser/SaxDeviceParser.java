package ua.lab2.devices.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.lab2.devices.model.Device;

/**
 * Parser that reads the document sequentially and reacts to events (SAX).
 */
public class SaxDeviceParser extends AbstractDeviceParser {

    @Override
    protected List<Device> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        DeviceEventCollector collector = new DeviceEventCollector();
        factory.newSAXParser().parse(in, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                collector.startElement(qName,
                        attributes.getValue(DeviceTag.ID_ATTRIBUTE));
            }

            @Override
            public void characters(char[] chars, int start, int length) {
                collector.characters(new String(chars, start, length));
            }

            @Override
            public void endElement(String uri, String localName,
                    String qName) {
                collector.endElement(qName);
            }
        });
        return collector.getDevices();
    }
}