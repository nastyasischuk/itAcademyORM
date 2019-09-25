package connection;

import annotations.Entity;
import exceptions.ConfigFileException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ParseXMLConfig {

    private File xmlConfigFile;

    ParseXMLConfig(String pathToXmlConfigFile) {
        this.xmlConfigFile = new File(pathToXmlConfigFile);
    }

    String getUrl() {
        return getProperty("database.connection.url").trim();
    }

    String getDriverClass() {
        return getProperty("database.connection.driver_class").trim();
    }

    String getUsername() {
        return getProperty("database.connection.username").trim();
    }

    String getPassword() {
        return getProperty("database.connection.password").trim();
    }

    private String getProperty(String propertyName) {
        NodeList properties = getNode("property");
        if (properties == null) {
            return null;
        }

        for (int counter = 0; counter < properties.getLength(); counter++) {
            Node currentProperty = properties.item(counter);
            if (currentProperty.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentProperty;
                if (element.getAttribute("name").equals(propertyName)) {
                    return properties.item(counter).getTextContent();
                }
            }
        }
        //throw ConfigException
        return null;
    }

    private NodeList getNode(String node) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(this.xmlConfigFile);
            doc.getDocumentElement().normalize();

            return doc.getElementsByTagName(node);
        } catch (Exception e) {
            throw new ConfigFileException(e.getMessage());
        }
    }

    List<Class<?>> getAllClasses() {
        List<Class<?>> configuredClassList = new ArrayList<>();
        NodeList properties = getNode("mapping");

        for (int counter = 0; counter < properties.getLength(); counter++) {
            Node currentProperty = properties.item(counter);
            if (currentProperty.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentProperty;
                try {
                    Class currentClass = Class.forName(element.getAttribute("name"));
                   if (currentClass.isAnnotationPresent(Entity.class)) {
                       configuredClassList.add(currentClass);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return configuredClassList;
    }

}
