package customQuery;


import CRUD.FindAllHandler;
import connection.DataBase;
import customQuery.QueryBuilder;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class Query<T> {
    private String queryToGetObject;
    private Class typeOgObjects;
    private DataBase dataBase;

    public Query(QueryBuilder customQuery) {
        this.queryToGetObject = customQuery.getQuery();
        this.dataBase = customQuery.getDataBase();
        typeOgObjects = customQuery.getClassType();
    }

    public T getSingleObject() {
        FindAllHandler findAllHandler = new FindAllHandler(dataBase, typeOgObjects);
        CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
        T result = (T) findAllHandler.buildObjectWithoutId(rowSet);
        return result;
    }

    public List<T> getListOfFoundObjects() {
        List<T> resultList = new ArrayList<>();
        FindAllHandler findAllHandler = new FindAllHandler(dataBase, typeOgObjects);
        CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
        resultList = (List<T>) findAllHandler.buildManyObjects(rowSet);
        return resultList;
    }

}
