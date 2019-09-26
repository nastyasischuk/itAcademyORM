package tablecreation;

import connection.DataBaseImplementation;

import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private DataBaseImplementation dataBaseImplementation;

    public void executeQuery(String query){
        dataBaseImplementation.openConnection();
        try {
            Statement statement = dataBaseImplementation.getConnection().createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            dataBaseImplementation.close();
        }
    }
}