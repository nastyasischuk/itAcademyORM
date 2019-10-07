package connection;

import annotations.Entity;
import exceptions.ConfigFileException;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ParseXMLConfig {
    private static Logger logger = Logger.getLogger(ParseXMLConfig.class);
    private static final String URL = "database.connection.url";
    private static final String DRIVER_CLASS = "database.connection.driver_class";
    private static final String USERNAME = "database.connection.username";
    private static final String PASSWORD = "database.connection.password";
    private static final String PROPERTY = "property";
    private static final String NAME = "name";
    private static final String PACKAGE = "scan.package";
    private File xmlConfigFile;

    public ParseXMLConfig(String pathToXmlConfigFile) {
        this.xmlConfigFile = new File(pathToXmlConfigFile);
    }

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

    private String getPackage() {
        return getProperty(PACKAGE).trim();
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

        Set<Class<?>> classes = findAllClassesInPackages(getPackage());

        for (Class<?> c : classes) {
            if (c.isAnnotationPresent(Entity.class)) {
                logger.debug("try to load class " + c.getName());
                Class currentClass = loadClass(c);
                logger.debug("Found and loaded class " + currentClass.getName());
                configuredClassList.add(currentClass);
            }
        }
//        NodeList properties = getNode(MAPPING);
//        for (int counter = 0; counter < properties.getLength(); counter++) {
//            Node currentProperty = properties.item(counter);
//            if (currentProperty.getNodeType() == Node.ELEMENT_NODE) {
//                Element element = (Element) currentProperty;
//                Class currentClass = loadClass(element);
//                if (currentClass.isAnnotationPresent(Entity.class)) {
//                    configuredClassList.add(currentClass);
//                }
//            }
//        }

        return configuredClassList;
    }

    private static Set<Class<?>> findAllClassesInPackages(String packageToScan) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageToScan))));

        return reflections.getSubTypesOf(Object.class);
    }

    private Class loadClass(Class currentClass) {
        String className = currentClass.getName();
        try {
            logger.debug("Try to load " + className);
            currentClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error(e.getException() + " " + e.getMessage());
            logger.error(e.getCause().getMessage());
        }
        return currentClass;
    }
}

