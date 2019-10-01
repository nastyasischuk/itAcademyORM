package tablecreation;

import connection.DataBaseImplementation;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private static Logger logger = Logger.getLogger(QueryExecutor.class);
    private DataBaseImplementation dataBaseImplementation;

    public void executeQuery(String query){
        dataBaseImplementation.openConnection();
        try {
            Statement statement = dataBaseImplementation.getConnection().createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally {
            dataBaseImplementation.close();
        }
    }
}