package connection;

import java.sql.Connection;

public interface DataBase {
    void openConnection();
    Connection getConnection();
    void close();

}
