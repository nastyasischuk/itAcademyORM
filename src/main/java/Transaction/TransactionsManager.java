package Transaction;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.sql.*;

public class TransactionsManager implements Transactions {
    private static final Logger log = Logger.getLogger(TransactionsManager.class);
    private static final String var1 = System.getProperty("user.dir") + File.separator + "log4j.properties";


    private Connection connection;

    public TransactionsManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void commit() {
            PropertyConfigurator.configure(var1);
        try {
            if (connection != null && !connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Committing JDBC Connection [" + connection + "]");
                }
                connection.commit();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void rollback() {
        PropertyConfigurator.configure(var1);
        try {
            if (connection != null && !connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Rolling back JDBC Connection [" + connection + "]");
                }
                connection.rollback();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void resetAutoCommit() {
        PropertyConfigurator.configure(var1);
        try {
            if (!connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Resetting autocommit to true on JDBC Connection [" + connection + "]");
                }
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error resetting autocommit to true "
                        + "before closing the connection.  Cause: " + e);
            }
        }
    }
}