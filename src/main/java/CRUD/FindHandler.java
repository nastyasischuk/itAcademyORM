package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.RowConstructorFromDB;
import CRUD.rowhandler.RowFromDB;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.sql.Statement;

public class FindHandler extends FindAllHandler {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(FindHandler.class);
    protected RowFromDB row;
    protected DataBase dataBase;
    protected Class objectType;
    protected Object idOfClassToFind;
    protected Statement statement;

    public FindHandler(DataBase dataBase, Class<?> objectType, Object id) {
        super(dataBase,objectType);
        this.dataBase = dataBase;
        this.objectType = objectType;
        this.idOfClassToFind = id;

    }
    public Object buildObject(CachedRowSet rowSet) {
        Object resultObject = null;
        try {
            rowSet.next();
            resultObject = new ObjectBuilder(row, rowSet, objectType, dataBase).buildObject();
        }catch (NullPointerException e){
            logger.error("Could not find object",e.getCause());
        }catch (Exception e) {
            logger.error(e, e.getCause());
        }
        dataBase.closeStatement(statement);
        return resultObject;
    }
    public String buildQuery() {
        row = new RowConstructorFromDB(objectType, idOfClassToFind).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row, QueryType.SELECT_OBJECT).buildQuery();
        return queryFind;
    }
}
