package CRUD.requests;



import CRUD.FindAllHandler;
import connection.DataBase;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class Query<T> {
String queryToGetObject;
Class typeOgObjects;
DataBase dataBase;
public Query(CustomQuery customQuery){
    //this.queryToGetObject = customQuery.getStr().toString();
    //this.database  = getDatabse;
    typeOgObjects = customQuery.getClass();
}

public T getSingleObject(){
    FindAllHandler findAllHandler = new FindAllHandler(dataBase,typeOgObjects);
    CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
    T result =(T) findAllHandler.buildObjectWithoutId(rowSet);
    return null;
}
public List<T> getListOfFoundObjects(){
    List<T> resultList = new ArrayList<>();
    FindAllHandler findAllHandler = new FindAllHandler(dataBase,typeOgObjects);
    CachedRowSet rowSet = findAllHandler.getResultSetFromQuery(queryToGetObject);
    List<T> r = (List<T>)findAllHandler.buildManyObjects(rowSet);
    return resultList;
}

}
