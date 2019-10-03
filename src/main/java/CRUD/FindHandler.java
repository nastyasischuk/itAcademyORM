package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.querycreation.QueryBuilderFactory;
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

public class FindHandler {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(FindHandler.class);
    protected  RowFromDB row;
    protected DataBase dataBase;
    protected Class objectType;
    protected Object idOfClassToFind;
    protected Statement statement;
    public FindHandler(DataBase dataBase,Class<?> objectType, Object id){
        this.dataBase = dataBase;
        this.objectType = objectType;
        this.idOfClassToFind = id;

    }

    public CachedRowSet getResultSetFromQuery(String queryFind){
         statement = dataBase.getStatement(queryFind);
        CachedRowSet rowset = null;
        try {
            ResultSet resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
            rowset = factory.createCachedRowSet();
            rowset.populate(resultSet);

        }catch (SQLException e){
            logger.info(e,e.getCause());
        }
        return rowset;
    }

    public String buildQuery(){
        row = new RowConstructorFromDB(objectType,idOfClassToFind).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();

        return queryFind;
    }

    public Object buildObject(CachedRowSet rowSet){
        Object resultObject=null;
        try {
            rowSet.next();
            resultObject = new ObjectBuilder(row, rowSet, objectType,dataBase).buildObject();
        }catch (Exception e){
            logger.error(e,e.getCause());
        }
        dataBase.closeStatement(statement);
        return resultObject;
    }
}
