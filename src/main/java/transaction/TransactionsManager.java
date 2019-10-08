package transaction;

import exceptions.TransactionException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.sql.*;

public class TransactionsManager implements Transactions {
    private static final Logger log = Logger.getLogger(TransactionsManager.class);
    private Connection connection;

    public TransactionsManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException var2) {
            throw new TransactionException(var2.getMessage(), var2);
        }
    }

    @Override
    public void commit() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Committing JDBC Connection [" + connection + "]");
                }
                connection.commit();
            }
        } catch (SQLException var2) {
            throw new TransactionException(var2.getMessage(), var2);
        }
    }

    @Override
    public void rollback() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                if (log.isDebugEnabled()) {
                    log.debug("Rolling back JDBC Connection [" + connection + "]");
                }
                connection.rollback();
            }
        } catch (SQLException var2) {
            throw new TransactionException(var2.getMessage(), var2);
        }
    }

    public void resetAutoCommit() {
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