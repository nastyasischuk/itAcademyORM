package CRUD;

import CRUD.exceptions.QueryExecutionException;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.*;

import annotations.*;


import connection.DataBaseImplementation;
import exceptions.NoPrimaryKeyException;
import org.apache.log4j.Logger;
import tablecreation.ManyToManyConstructor;
import tablecreation.SQLStatements;

import javax.sql.rowset.CachedRowSet;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;


public class CRUDImpl implements CRUD {
    private static Logger logger = Logger.getLogger(CRUDImpl.class);
    private DataBaseImplementation dataBase;

    public CRUDImpl(DataBaseImplementation dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void save(Object objectToDB) {

        List<Object> ids = checkAndSaveInnerEntities(objectToDB);
        RowToDB rowToDB = null;
        try {
            rowToDB = cudBasics(objectToDB, QueryType.INSERT);
        } catch (SQLException e) {
            //todo change
        }
        if (rowToDB.isAutoIncrement()) {
            Object calculatedId = queryId(rowToDB, QueryType.SELECTID);
            try {
                setIdToObject(objectToDB, calculatedId);
            } catch (NoPrimaryKeyException e) {
                logger.error("Primary key is not found");
            }
        }

        try {
            List<Object> objectsOTOToSave = getAllObjects(objectToDB, OneToOne.class);
            logger.debug("GET ALL OBJECTS WITH OTO");
            for (Object o : objectsOTOToSave) {
                setIdToObject(o, getValueOfPK(objectToDB));
                logger.debug("GOT ID OF ORIGINAL " + getValueOfPK(objectToDB).toString());
                logger.debug("Found object oto " + o.toString());
                save(o);
            }
        } catch (IllegalAccessException | NoPrimaryKeyException e) {
            logger.error(e.getMessage());
        }


        Object idMain = getValueOfPK(objectToDB);
        Field[] fields = objectToDB.getClass().getDeclaredFields();
        for (Field field : fields)
            if (field.isAnnotationPresent(ManyToMany.class) && field.isAnnotationPresent(AssociatedTable.class)) {
                try {
                    tablecreation.ManyToMany manyToMany = new ManyToManyConstructor(field).build();
                    for (Object id : ids) {
                        logger.debug("ID = " + id + " ID MAIN = " + idMain);
                        String query = createQuery(id, idMain, manyToMany);
                        logger.debug("Executing query " + query);
                        dataBase.executeUpdateQuery(query);
                    }
                } catch (NoPrimaryKeyException e) {
                    logger.error("No primary key");
                } catch (SQLException e) {
                    logger.error(e,e.getCause());
                    throw new QueryExecutionException("Could not update existing rows",e);
                }
            }

    }

    private String createQuery(Object idSide, Object idMain, tablecreation.ManyToMany mtm) {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue())
                .append(mtm.getAssociatedTableName()).append(" ");
        query.append("( ").append(mtm.getForeignKeyToOriginalTableName()).append(", ")
                .append(mtm.getForeignKeyToLinkedTableName()).append(" )").append(SQLStatements.VALUES.getValue()).append("( ");
        query.append(idMain.toString()).append(", ").append(idSide.toString()).append(" );");

        return query.toString();
    }

    private List<Object> checkAndSaveInnerEntities(Object objectToDB) {
        List<Object> ids = null;
        try {
            List<Object> objectsToSaveBefore = getAllObjects(objectToDB, ManyToOne.class);
            logger.debug("GET ALL OBJECTS WITH MTO");
            for (Object o : objectsToSaveBefore) {
                logger.debug(o.toString());
                if (checkIfNotInDB(o, getValueOfPK(o)))
                    save(o);
            }

            List<Object> collectionsToSaveBefore = getAllObjects(objectToDB, ManyToMany.class);
            ids = new ArrayList<>();
            for (Object currentCollection : collectionsToSaveBefore) {
                if (currentCollection instanceof Collection) {
                    Iterator items = ((Collection) currentCollection).iterator();
                    while (items != null && items.hasNext()) {
                        Object o = items.next();
                        ids.add(getValueOfPK(o));
                        logger.debug("PK OF THIS" + getValueOfPK(o));
                        if (checkIfNotInDB(o, getValueOfPK(o))) {
                            logger.debug("SAVING THIS SHIT -> " + o.toString());
                            save(o);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return ids;
    }

    @Override
    public void delete(Object objectToDelete) {
        try {
            cudBasics(objectToDelete, QueryType.DELETE);
        }catch (SQLException e){
            throw new QueryExecutionException("Could not delete ",e);
        }
    }

    @Override
    public void update(Object objectToUpdate) {
        try {
            cudBasics(objectToUpdate, QueryType.UPDATE);
        }catch (SQLException e){
            throw new QueryExecutionException("Could not update ",e);
        }
    }

    private RowToDB cudBasics(Object objectToDB, QueryType queryType) throws SQLException{
        RowToDB rowToDB = new RowConstructorToDB(objectToDB).buildRow();
        String query = new QueryBuilderFactory().createQueryBuilder(rowToDB, queryType).buildQuery();
        dataBase.executeUpdateQuery(query);
        return rowToDB;
    }

    private Object queryId(RowToDB row, QueryType queryType) {
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        try (Statement statement = dataBase.getStatement(query)) {
            ResultSet resultSet;
            resultSet = statement.executeQuery(query);
            Object ob = null;
            while (resultSet.next()) {
                ob = resultSet.getObject(1);
            }
            resultSet.close();
            statement.close();
            return ob;
        } catch (SQLException e) {
            logger.error(e,e.getCause());
        }
        return null;
    }

    private String getNameOfPrimaryKey(Field[] fields) throws NoPrimaryKeyException {
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return field.getName();
            }
        }
        throw new NoPrimaryKeyException();
    }

    public void setIdToObject(Object object, Object idToObject) throws NoPrimaryKeyException {
        Field[] fields = object.getClass().getDeclaredFields();
        String nameOfId = getNameOfPrimaryKey(fields);
        Field field;
        try {
            field = object.getClass().getDeclaredField(nameOfId);
            field.setAccessible(true);

            field.set(object,idToObject);
        }catch (NoSuchFieldException | IllegalAccessException e){
           logger.error(e);

        }
    }
  
    public Object find(Class<?> objectType, Object id){
        FindHandler findHandler = new FindHandler(dataBase,objectType,id);
        return getBuiltObject(findHandler);
    }
    @Override
    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping) {
        FindHandler findHandler = new FindHandlerCollection(dataBase,classToFind,id,usingForeignKey,mapping);
      return (Collection<Object>) getBuiltObject(findHandler);
    }
    @Override
    public Collection<Object> findCollectionFoManyToMany(Class classToFind, Object id, AssociatedTable associatedTable) {
        FindHandler findHandler = new FindHandlerManyToMany(dataBase,classToFind,id,associatedTable);
        return (Collection<Object>) getBuiltObject(findHandler);
    }

    private Object getBuiltObject(FindHandler findHandler){
        String query = findHandler.buildQuery();
        CachedRowSet cachedRowSet = findHandler.getResultSetFromQuery(query);
        return findHandler.buildObject(cachedRowSet);
    }


    private List<Object> getAllObjects(Object original, Class<? extends Annotation> annotation) throws IllegalAccessException {
        List<Object> allObjects = new ArrayList<>();
        Field[] fields = original.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (annotation.isAssignableFrom(OneToOne.class)) {
                if (field.isAnnotationPresent(annotation) && !field.isAnnotationPresent(MapsId.class)) {
                    field.setAccessible(true);
                    logger.debug("FOUND OBJECT " + field.get(original).toString());
                    allObjects.add(field.get(original));
                }
            } else if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                logger.debug("FOUND OBJECT " + field.get(original).toString());
                allObjects.add(field.get(original));
            }
        }
        return allObjects;
    }

    private boolean checkIfNotInDB(Object someObject, Object id) {
        return find(someObject.getClass(), id) == null;
    }

    private Object getValueOfPK(Object insideOriginal) {
        Object valueOfPK = "";
        try {
            Field[] fields = insideOriginal.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                    field.setAccessible(true);
                    valueOfPK = field.get(insideOriginal);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return valueOfPK;
    }

}
