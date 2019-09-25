package tablecreation;

import connection;

import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private DataBaseImplementation dataBaseImplementation;

    public void executeQuery(String query){
        dataBaseImplementation.openConnection();
        Statement statement = dataBaseImplementation.getConnection().createStatement();
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            dataBaseImplementation.close();
        }
    }
}