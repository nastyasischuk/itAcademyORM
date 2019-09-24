package connection;

import exceptions.DatabaseException;
import exceptions.OpenConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseImplementation implements DataBase {

    private ParseXMLConfig parseXMLConfig;
    private static final String DEFAULT = "default_db";
    private final String name;

    public DataBaseImplementation(String pathToXml) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        this.name = DEFAULT;
    }

    public DataBaseImplementation(String pathToXml, String name) {
        this.name = name;
    }

    public void openConnection() {
        this.checkExistingConnection(this.name);
        
        try {
            Class.forName(parseXMLConfig.getDriverClass());
            Connection connection = DriverManager.getConnection(parseXMLConfig.getUrl(),
                    parseXMLConfig.getUsername(), parseXMLConfig.getPassword());
            OpenedConnection.addConnection(this.name, connection);
        } catch (SQLException | ClassNotFoundException e) {
            throw new OpenConnectionException(e.getMessage());
        }
    }

    private void checkExistingConnection(String name) {
        if (null != OpenedConnection.getConnection(name)) {
            throw new OpenConnectionException("Connection " + name + "is already existing.");
        }
    }

    public Connection getConnection() {
        Connection connection = OpenedConnection.getConnection(this.name);
        if (connection == null) {
            throw new DatabaseException("Connection does not exist.");
        } else {
            return connection;
        }
    }

    public void close() {
        try {
            Connection connection = OpenedConnection.getConnection(this.name);
            if (connection == null) {
                throw new DatabaseException("Cannot close connection.");
            }
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            OpenedConnection.removeConnection(this.name);
        }
    }
}
