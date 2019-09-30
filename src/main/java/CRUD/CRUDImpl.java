package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.buildingObject.ObjectBuilderWithLinks;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.*;
import annotations.AssociatedTable;
import annotations.OneToMany;
import annotations.PrimaryKey;
import connection.DataBaseImplementation;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class CRUDImpl implements CRUD {

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
        dataBase.executeQuery(query);
        return rowToDB;
    }
    private ResultSet queryId(RowToDB row,QueryType queryType){
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        return null;
        //todo execute update  dataBase.executeQuery(query);
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
        ResultSet resultSet = dataBase.executeQueryWithResult(queryFind);
        Object resultObject=null;
        try {
            resultSet.next();
             resultObject = new ObjectBuilder(row, resultSet, objectType,dataBase).buildObject();
        }catch (Exception e){
            //todo logger check
        }

        return resultObject;
    }
    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping) {
        RowFromDB row = new RowConstructorFromDBByForeignKey(classToFind,id,usingForeignKey.getClass()).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        ResultSet resultSet = dataBase.executeQueryWithResult(queryFind);
        Collection<Object> collection = new HashSet<>();
        //if(checkIfOneToMany(classToFind)){
        //
        //}else
        try {
            while (resultSet.next()) {
                collection.add(new ObjectBuilderWithLinks(row, resultSet, classToFind, usingForeignKey, mapping,dataBase).buildObject());
            }
        }catch (Exception e){
            //todo handle exception
        }
        return collection;
    }





    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping, AssociatedTable associatedTable) {
        RowFromDB row = new RowConstructorFromDBByForeignKey(classToFind,id,usingForeignKey.getClass()).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        ResultSet resultSet = dataBase.executeQueryWithResult(queryFind);
        Collection<Object> collection = new HashSet<>();
        try {
            while (resultSet.next()) {
                collection.add(new ObjectBuilderWithLinks(row, resultSet, classToFind, usingForeignKey, mapping,dataBase).buildObject());
            }
        }catch (Exception e){
            //todo handle exception
        }
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
