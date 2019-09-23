package connection;

import exceptions.OpenConnectionException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenedConnection {
    private static final ThreadLocal<HashMap<String, Connection>> openedConnections = new ThreadLocal<>();

    public OpenedConnection() {
    }

    private static Map<String, Connection> getConnectionMap() {
        if (openedConnections.get() == null) {
            openedConnections.set(new HashMap<>());
        }

        return openedConnections.get();
    }

    static Connection getConnection(String name) {
        return getConnectionMap().get(name);
    }

    static void addConnection(String name, Connection connection) {
        if (getConnectionMap().get(name) != null) {
            throw new OpenConnectionException("Connection " + name + "is already existing.");
        } else {
            getConnectionMap().put(name, connection);
        }
    }

    static void removeConnection(String name) {
        getConnectionMap().remove(name);
    }

    static ArrayList<Connection> getAllConnections() {
        return new ArrayList<>(getConnectionMap().values());
    }
}
