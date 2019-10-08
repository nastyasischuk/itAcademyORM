package customQuery;


import CRUD.FindAllHandler;
import CRUD.FindHandlerCollection;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class QueryResult<T> {
    private static org.apache.log4j.Logger logger = Logger.getLogger(QueryResult.class);
    private String queryToGetObject;
    private Class typeOgObjects;
    private DataBase dataBase;

    public QueryResult(QueryBuilder customQuery) {
        this.queryToGetObject = customQuery.getQuery();
        this.dataBase = customQuery.getDataBase();
        typeOgObjects = customQuery.getClassType();
    }

    public T getSingleObject() {
        FindAllHandler findAllHandler = new FindAllHandler(dataBase, typeOgObjects);
        CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
        try {
            rowSet.next();
        }catch (Exception e){
            logger.error(e,e.getCause());
        }
        T result = (T) findAllHandler.buildObjectWithoutId(rowSet);
        return result;
    }

    public List<T> getListOfFoundObjects() {
        List<T> resultList = new ArrayList<>();
        FindAllHandler findAllHandler = new FindAllHandler(dataBase, typeOgObjects);
        CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
       try {
           while (rowSet.next()) {
               resultList.add((T) findAllHandler.buildObjectWithoutId(rowSet));
           }
       }catch (Exception e){
           logger.error(e,e.getCause());
       }
        return resultList;
    }

}
