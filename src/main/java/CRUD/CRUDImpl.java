package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import CRUD.buildingObject.ObjectBuilderWithLinks;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.*;
import annotations.*;
import connection.DataBaseImplementation;
import exceptions.NoPrimaryKeyException;
import javafx.scene.SceneAntialiasing;
import org.apache.log4j.Logger;
import tablecreation.ManyToManyConstructor;
import tablecreation.SQLStatements;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
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
        RowToDB rowToDB = cudBasics(objectToDB, QueryType.INSERT);
        if (rowToDB.isAutoIncrement()) {
            Object calculatedId = queryId(rowToDB, QueryType.SELECTID);
            try {
                setIdToObject(objectToDB, calculatedId);
            } catch (NoPrimaryKeyException e) {
                logger.error("Primary key is not found");
            }
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
                if (!checkIfAlreadyInDB(o, getValueOfPK(o)))
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
                        if (!checkIfAlreadyInDB(o, getValueOfPK(o))) {
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
        cudBasics(objectToDelete, QueryType.DELETE);
    }

    @Override
    public void update(Object objectToUpdate) {
        cudBasics(objectToUpdate, QueryType.UPDATE);
    }

    private RowToDB cudBasics(Object objectToDB, QueryType queryType) {
        RowToDB rowToDB = new RowConstructorToDB(objectToDB).buildRow();
        String query = new QueryBuilderFactory().createQueryBuilder(rowToDB, queryType).buildQuery();
        dataBase.executeUpdateQuery(query);
        return rowToDB;
    }

    private Object queryId(RowToDB row, QueryType queryType) {
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        try (Statement statement = dataBase.createStatementForQueryWithResult(query)) {
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
            logger.error("We are fucked with Result Set while getting latest id");
        }
        return null; //TODO Change mb later?
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
            field.set(object, idToObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage() + " " + e.getCause().getMessage());
        }
    }

    private Object calculateId(ResultSet resultSet) {
        try {
            logger.debug("ResultSet is " + resultSet.toString());
            return resultSet.getObject(1);
        } catch (SQLException e) {
            logger.error(e.getMessage() + " " + e.getCause().getMessage());
            return null;
        }
    }

    @Override
    public Object find(Class<?> objectType, Object id) {
        RowFromDB row = new RowConstructorFromDB(objectType, id).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        Statement statement = dataBase.createStatementForQueryWithResult(queryFind);
        CachedRowSet rowset = null;
        try {
            ResultSet resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
            rowset = factory.createCachedRowSet();
            rowset.populate(resultSet);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        Object resultObject = null;
        try {
            rowset.next();
            resultObject = new ObjectBuilder(row, rowset, objectType, dataBase).buildObject();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            dataBase.closeStatement(statement);
        }

        return resultObject;
    }

    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping) {
        RowFromDB row = new RowConstructorFromDBByForeignKey(classToFind, id, usingForeignKey.getClass()).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row).buildQuery();
        Statement statement = dataBase.createStatementForQueryWithResult(queryFind);
        Collection<Object> collection = new HashSet<>();
        CachedRowSet rowset = null;
        try {
            ResultSet resultSet = statement.executeQuery(queryFind);
            RowSetFactory factory = RowSetProvider.newFactory();
            rowset = factory.createCachedRowSet();
            rowset.populate(resultSet);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        try {
            while (rowset.next()) {
                Object object = new ObjectBuilderWithLinks(row, rowset, classToFind, usingForeignKey, mapping, dataBase).buildObject();
                logger.info(object);
                collection.add(object);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        dataBase.closeStatement(statement);
        return collection;
    }

    private boolean checkIfOneToMany(Class<?> cllasToTest) {
        Field[] allFields = cllasToTest.getDeclaredFields();
        for (Field field : allFields)
            if (field.isAnnotationPresent(OneToMany.class))
                return true;
        return false;
    }

    private List<Object> getAllObjects(Object original, Class annotation) throws IllegalAccessException {
        List<Object> allObjects = new ArrayList<>();
        Field[] fields = original.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                allObjects.add(field.get(original));
            }
        }
        return allObjects;
    }

    private List<Object> getAllObjectsMTM(Object original) throws IllegalAccessException {
        List<Object> allObjects = new ArrayList<>();
        Field[] fields = original.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToMany.class) /*&& field.isAnnotationPresent(AssociatedTable.class)*/) {
                field.setAccessible(true);
                allObjects.add(field.get(original));
            }
        }
        return allObjects;
    }

    private boolean checkIfAlreadyInDB(Object someObject, Object id) {
        return find(someObject.getClass(), id) != null;
    }

    private String getValueOfPK(Object insideOriginal) {
        String valueOfPK = "";
        try {
            Field[] fields = insideOriginal.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                    field.setAccessible(true);
                    valueOfPK = field.get(insideOriginal).toString();
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return valueOfPK;
    }
}
