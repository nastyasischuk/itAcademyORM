package CRUD;
import CRUD.buildingObject.ObjectBuilder;
import CRUD.rowhandler.RowConstructorFromDB;
import CRUD.rowhandler.RowFromDB;
import connection.DataBase;
import org.apache.log4j.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class FindAllHandler {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(FindAllHandler.class);
    private RowFromDB row;
    protected DataBase dataBase;
    protected Class objectType;
    protected Statement statement;

    public FindAllHandler(DataBase dataBase, Class objectType) {
        this.dataBase = dataBase;
        this.objectType = objectType;

    }

    public CachedRowSet getResultSetFromQuery(String queryFind) {
        statement = dataBase.getStatement(queryFind);
        CachedRowSet rowset = null;
        try {
            ResultSet resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
            rowset = factory.createCachedRowSet();
            rowset.populate(resultSet);

        } catch (SQLException e) {
            logger.info(e, e.getCause());
        }
        return rowset;
    }
    public Object buildObjectWithoutId(CachedRowSet rowSet){
        this.row = new RowConstructorFromDB(objectType).buildRow();
        Object resultObject = null;
        try {

            resultObject = new ObjectBuilder(row, rowSet, objectType, dataBase).buildObject();
        }catch (NullPointerException e){
            logger.error("Could not find object",e.getCause());
        }catch (Exception e) {
            logger.error(e, e.getCause());
        }
        dataBase.closeStatement(statement);
        return resultObject;
    }

   public List<Object> buildManyObjects(CachedRowSet rowSet) {
       this.row = new RowConstructorFromDB(objectType).buildRow();
       Object resultObject = null;
       List<Object> objects = null;
       try {
          while(rowSet.next()) {
              resultObject = new ObjectBuilder(row, rowSet, objectType, dataBase).buildObject();
              objects.add(resultObject);
          }
       }catch (NullPointerException e){
           logger.error("Could not find object",e.getCause());
       }catch (Exception e) {
           logger.error(e, e.getCause());
       }
       dataBase.closeStatement(statement);
       return objects;
   }

}
