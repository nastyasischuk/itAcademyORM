package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.buildingObject.ObjectBuilderWithLinks;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.*;
import annotations.OneToMany;
import annotations.PrimaryKey;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

public class CRUDImpl implements CRUD {
    private static Logger logger = Logger.getLogger(CRUDImpl.class);
    private DataBaseImplementation dataBase;

    public CRUDImpl(DataBaseImplementation dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void save(Object objectToDB) {
        RowToDB rowToDB = cudBasics(objectToDB, QueryType.INSERT);
        if (rowToDB.isAutoIncrement()) {
          ResultSet idTable = queryId(rowToDB,QueryType.INSERT);
        Object calculatedId = calculateId(idTable);
            setIdToObject(objectToDB,calculatedId);
        }

    }

    @Override
    public void delete(Object objectToDelete) {
        cudBasics(objectToDelete,QueryType.DELETE);
    }

    @Override
    public void update(Object objectToUpdate) {
        cudBasics(objectToUpdate,QueryType.UPDATE);
    }

    private RowToDB cudBasics(Object objectToDB, QueryType queryType){
        RowToDB rowToDB = new RowConstructorToDB(objectToDB).buildRow();
        String query = new QueryBuilderFactory().createQueryBuilder(rowToDB, queryType).buildQuery();
        dataBase.executeUpdateQuery(query);
        return rowToDB;
    }
    private ResultSet queryId(RowToDB row,QueryType queryType){
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        return null;
        //todo execute update  dataBase.executeUpdateQuery(query);
    }
    private  String getNameOfPrimaryKey(Field fields[]){
        for(int i=0;i<fields.length;i++){
            if(fields[i].isAnnotationPresent(PrimaryKey.class)){
                return fields[i].getName();
            }
        }
        return null;//todo change;
    }
    public  void setIdToObject(Object object,Object idToObject){
        Field fields[] = object.getClass().getDeclaredFields();
        String nameOfId = getNameOfPrimaryKey(fields);
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(nameOfId);
            field.setAccessible(true);
            field.set(object,idToObject);
        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
            //todo add logger exception
        }
    }
    private Object calculateId(ResultSet resultSet){
        try {
            return resultSet.getObject(1);
        }catch (SQLException e){
            //todo handle exception
            return null;
        }
    }

    @Override
    public Object find(Class<?> objectType, Object id){
        RowFromDB row = new RowConstructorFromDB(objectType,id).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        Statement statement = dataBase.executeQueryWithResult(queryFind);
        CachedRowSet rowset = null;
        try {
            ResultSet resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
             rowset = factory.createCachedRowSet();
             rowset.populate(resultSet);
        }catch (Exception e){
            logger.error(e.getMessage());
        }


        Object resultObject=null;
        try {
            rowset.next();
             resultObject = new ObjectBuilder(row, rowset, objectType,dataBase).buildObject();
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            dataBase.closeStatement(statement);
        }
        return resultObject;
    }
    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping) {
        RowFromDB row = new RowConstructorFromDBByForeignKey(classToFind,id,usingForeignKey.getClass()).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        Statement statement = dataBase.executeQueryWithResult(queryFind);
        Collection<Object> collection = new HashSet<>();
        CachedRowSet rowset = null;
        try {
            ResultSet  resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
            rowset = factory.createCachedRowSet();
            rowset.populate(resultSet);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        try {
           while (rowset.next()) {
               Object object = new ObjectBuilderWithLinks(row, rowset, classToFind, usingForeignKey, mapping,dataBase).buildObject();
               logger.info(object);
               collection.add(object);
           }

       }catch (Exception e){
           logger.error(e.getMessage());
       }
        dataBase.closeStatement(statement);
        return collection;
    }
    private boolean checkIfOneToMany(Class<?> cllasToTest){
        Field[] allFields = cllasToTest.getDeclaredFields();
        for(Field field :allFields)
            if(field.isAnnotationPresent(OneToMany.class))
                return true;
            return false;
    }






}
