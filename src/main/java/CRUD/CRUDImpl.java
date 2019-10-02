package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.buildingObject.ObjectBuilderWithLinks;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.*;
import annotations.OneToMany;
import annotations.PrimaryKey;
import connection.DataBaseImplementation;
import exceptions.NoPrimaryKeyException;
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
            Object calculatedId = queryId(rowToDB, QueryType.SELECTID);
            try {
                setIdToObject(objectToDB, calculatedId);
            } catch (NoPrimaryKeyException e) {
                logger.error("Primary key is not found");
            }
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
        System.out.println(rowToDB);
        String query = new QueryBuilderFactory().createQueryBuilder(rowToDB, queryType).buildQuery();
        System.out.println("Query  " + query);
        dataBase.executeUpdateQuery(query);
        return rowToDB;
    }
    private Object queryId(RowToDB row, QueryType queryType){
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        System.out.println("QUERY WTF IS " + query); //this is INSERT
        Statement statement = dataBase.createStatementForQueryWithResult(query);
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
            Object ob = null;
            while(resultSet.next()){
                ob = resultSet.getObject(1);
            }
            resultSet.close();
            statement.close();
            return ob;
        } catch (SQLException e) {
            logger.error("We are fucked with Result Set while getting latest id");
        } finally {
            try{
                if(statement!= null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
        }
        return null; //TODO Change mb later?
    }
    private  String getNameOfPrimaryKey(Field[] fields) throws NoPrimaryKeyException {
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return field.getName();
            }
        }
        throw new NoPrimaryKeyException();
    }
    public  void setIdToObject(Object object, Object idToObject) throws NoPrimaryKeyException {
        Field[] fields = object.getClass().getDeclaredFields();
        String nameOfId = getNameOfPrimaryKey(fields);
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(nameOfId);
            field.setAccessible(true);
            field.set(object, idToObject);
        }catch (NoSuchFieldException | IllegalAccessException e){
            logger.error(e.getMessage() + " " + e.getCause().getMessage());
        }
    }
    private Object calculateId(ResultSet resultSet){
        try {
            logger.debug("ResultSet is " + resultSet.toString());
            return resultSet.getObject(1);
        }catch (SQLException e){
            logger.error(e.getMessage() + " " + e.getCause().getMessage());
            return null;
        }
    }

    @Override
    public Object find(Class<?> objectType, Object id){
        RowFromDB row = new RowConstructorFromDB(objectType,id).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        Statement statement = dataBase.createStatementForQueryWithResult(queryFind);
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
        Statement statement = dataBase.createStatementForQueryWithResult(queryFind);
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
