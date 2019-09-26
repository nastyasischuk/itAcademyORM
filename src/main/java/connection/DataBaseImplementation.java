package connection;

import exceptions.DatabaseException;
import exceptions.NoPrimaryKeyException;
import exceptions.OpenConnectionException;
import exceptions.SeveralPrimaryKeysException;
import org.apache.log4j.Logger;
import tablecreation.SQLTableQueryCreator;
import tablecreation.TableConstructorImpl;
import transaction.TransactionsManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseImplementation implements DataBase {
    private static Logger logger = Logger.getLogger(DataBaseImplementation.class);
    private static TransactionsManager transactionsManager = null;

    private ParseXMLConfig parseXMLConfig;
    private static final String DEFAULT = "default_db";
    private final String name;

    public DataBaseImplementation(String pathToXml) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        this.name = DEFAULT;
        createAllTables();
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
            logger.debug("Connection has opened " + connection);
            OpenedConnection.addConnection(this.name, connection);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage());
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
            logger.debug("Closed connection: {}" + connection);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            OpenedConnection.removeConnection(this.name);
        }
    }

    private void createAllTables() {
        List<String> fkQueriesToExecute = new ArrayList<>();
        List<Class<?>> allEntities = parseXMLConfig.getAllClasses();
        for (Class currentClass : allEntities) {
            tablecreation.Table table = null;
            try {
                table = new TableConstructorImpl(currentClass).buildTable();
            } catch (NoPrimaryKeyException e) {
                e.printStackTrace();
            } catch (SeveralPrimaryKeysException e) {
                e.printStackTrace();
            }
            SQLTableQueryCreator sqlTableQueryCreator = new SQLTableQueryCreator(table);
            String createTableQuery = sqlTableQueryCreator.createTableQuery();
            String createPKQuery = sqlTableQueryCreator.createPKQuery();
            fkQueriesToExecute.addAll(sqlTableQueryCreator.createFKQuery());

            executeQuery(createTableQuery);
            executeQuery(createPKQuery);
        }

        for (String query : fkQueriesToExecute) {
            executeQuery(query);
        }
    }

    private void executeQuery(String query){
        this.openConnection();
        Statement statement = null;
        try {
            statement = this.getConnection().createStatement();
            logger.debug("Executing query " + query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TransactionsManager getTransactionManager() {
        if (transactionsManager == null)
            transactionsManager = new TransactionsManager(this.getConnection());
        return transactionsManager;
    }
}
