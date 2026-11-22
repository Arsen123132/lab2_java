package ua.lab2.devices.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ua.lab2.devices.model.Device;

/**
 * Parser that loads the whole document into memory (DOM).
 */
public class DomDeviceParser extends AbstractDeviceParser {

    @Override
    protected List<Device> doParse(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Document document = factory.newDocumentBuilder().parse(in);

        NodeList deviceNodes =
                document.getElementsByTagName(DeviceTag.DEVICE.getXmlName());
        DeviceBuilder builder = new DeviceBuilder();
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < deviceNodes.getLength(); i++) {
            Element deviceElement = (Element) deviceNodes.item(i);
            builder.startDevice(deviceElement.getAttribute(DeviceTag.ID_ATTRIBUTE));
            readChildren(deviceElement, builder);
            devices.add(builder.build());
        }
        return devices;
    }

    private void readChildren(Element parent, DeviceBuilder builder) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element element) {
                builder.setValue(element.getNodeName(), element.getTextContent());
                readChildren(element, builder);
            }
        }
    }
}