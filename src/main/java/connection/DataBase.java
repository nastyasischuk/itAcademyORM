package connection;

import CRUD.CRUD;

import java.sql.Connection;
import java.sql.Statement;

public interface DataBase {
    void openConnection();
    Connection getConnection();
    void close();
    Statement getStatement(String s);
    CRUD getCrud();
    void closeStatement(Statement statement);

}
