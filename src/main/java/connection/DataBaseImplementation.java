package connection;

import CRUD.CRUDImpl;
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
    private CRUDImpl crud;

    private ParseXMLConfig parseXMLConfig;
    private static final String DEFAULT = "default_db";
    private final String name;


    public DataBaseImplementation(String pathToXml) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        crud = new CRUDImpl(this);
        this.name = DEFAULT;
        createAllTables();
    }

    public DataBaseImplementation(String pathToXml, boolean createTables) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        crud = new CRUDImpl(this);
        this.name = DEFAULT;
        if (createTables)
            createAllTables();
    }

    public DataBaseImplementation(String pathToXml, String name) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        crud = new CRUDImpl(this);
        this.name = name;
        createAllTables();
    }

    public DataBaseImplementation(String pathToXml, String name, boolean createTables) {
        parseXMLConfig = new ParseXMLConfig(pathToXml);
        crud = new CRUDImpl(this);
        this.name = name;
        if (createTables)
            createAllTables();
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
            logger.error(e,e.getCause());
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
            logger.error(e.getMessage());
            throw new DatabaseException(e.getMessage());
        } finally {
            OpenedConnection.removeConnection(this.name);
        }
    }

    private void createAllTables() {
        List<String> fkQueriesToExecute = new ArrayList<>();
        List<String> mtmQueriesToExecute = new ArrayList<>();

        List<Class<?>> allEntities = parseXMLConfig.getAllClasses();
        for (Class currentClass : allEntities) {
            tablecreation.Table table = null;
            try {
                logger.debug("Current class " + currentClass);
                table = new TableConstructorImpl(currentClass).buildTable();
            } catch (NoPrimaryKeyException | SeveralPrimaryKeysException e) {
                logger.error(e.getMessage());
            }
            SQLTableQueryCreator sqlTableQueryCreator = new SQLTableQueryCreator(table);
            String createTableQuery = sqlTableQueryCreator.createTableQuery();

            List<String> queriesFK = sqlTableQueryCreator.createFKQuery();
            if (queriesFK != null && !queriesFK.isEmpty())
                fkQueriesToExecute.addAll(queriesFK);
            List<String> queriesMTM = sqlTableQueryCreator.createManyToManyQuery();
            if (queriesMTM != null && !queriesMTM.isEmpty())
                mtmQueriesToExecute.addAll(queriesMTM);

            executeQueryForCreateDB(createTableQuery);
            //executeQueryForCreateDB(createPKQuery); //todo remove this later
        }

        logger.debug("Size og fks = " + fkQueriesToExecute.size());
        for (String query : fkQueriesToExecute) {
            logger.debug("Executing query for FK " + query);
            executeQueryForCreateDB(query);
        }

        logger.debug("Size og mtms = " + mtmQueriesToExecute.size());
        for (String query : mtmQueriesToExecute) {
            logger.debug("Executing query for MTM " + query);
            executeQueryForCreateDB(query);
        }
    }

    private void executeQueryForCreateDB(String query) {
        this.openConnection();
        Statement statement = null;
        try {
            statement = this.getConnection().createStatement();
            logger.debug("Executing query " + query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                this.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public CRUDImpl getCrud() {
        return crud;
    }

    public void executeUpdateQuery(String query) throws SQLException {
        Statement statement = null;
        try {
            statement = this.getConnection().createStatement();
            logger.debug("Executing query " + query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public Statement getStatement(String query) {
        Statement statement = null;
        try {
            statement = this.getConnection().createStatement();
            logger.debug("Executing query " + query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
        return statement;
    }

    public void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public TransactionsManager getTransactionManager() {
        if (transactionsManager == null)
            transactionsManager = new TransactionsManager(this.getConnection());
        return transactionsManager;
    }
}
