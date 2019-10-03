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

public class ParseXMLConfig {
        public static final String URL = "database.connection.url";
    public static final String DRIVER_CLASS = "database.connection.driver_class";
    public static final String USERNAME = "database.connection.username";
    public static final String PASSWORD = "database.connection.password";
    public static final String PROPERTY = "property";
    public static final String NAME = "name";
    public static final String MAPPING = "mapping";
    public static final String CLASS = "class";


    private File xmlConfigFile;

    ParseXMLConfig(String pathToXmlConfigFile) {
        this.xmlConfigFile = new File(pathToXmlConfigFile);
    }
// TODO move strings to constants
    String getUrl() {
        return getProperty(URL).trim();
    }

    String getDriverClass() {
        return getProperty(DRIVER_CLASS).trim();
    }

    String getUsername() {
        return getProperty(USERNAME).trim();
    }

    String getPassword() {
        return getProperty(PASSWORD).trim();
    }

    private String getProperty(String propertyName) {
        NodeList properties = getNode(PROPERTY);
        if (properties == null) {
            return null;
        }

        for (int counter = 0; counter < properties.getLength(); counter++) {
            Node currentProperty = properties.item(counter);
            if (currentProperty.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentProperty;
                if (element.getAttribute(NAME).equals(propertyName)) {
                    return properties.item(counter).getTextContent();
                }
            }
        }
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
        NodeList properties = getNode(MAPPING);

        for (int counter = 0; counter < properties.getLength(); counter++) {
            Node currentProperty = properties.item(counter);
            if (currentProperty.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentProperty;
                Class currentClass = loadClass(element);
                if (currentClass.isAnnotationPresent(Entity.class)) {
                    configuredClassList.add(currentClass);
                }
            }
        }
        return configuredClassList;
    }

    private Class loadClass(Element element) {
        Class currentClass = null;
        String className = element.getAttribute(CLASS).trim();
        try {
            //todo System.out.println("Try to load " + className );
            currentClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getCause().printStackTrace();
        }
        return currentClass;
    }

}
