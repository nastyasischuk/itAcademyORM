package CRUD;

import exceptions.QueryExecutionException;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.RowToDB;
import annotations.*;
import exceptions.NoPrimaryKeyException;
import org.apache.log4j.Logger;
import tablecreation.ManyToManyConstructor;
import tablecreation.SQLStatements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static annotations.AnnotationUtils.classGetTypeOfCollectionField;

public class SaveDelegate {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(SaveDelegate.class);

    private CRUDImpl crud;
    private Object objectToDB;

    SaveDelegate(CRUDImpl crud, Object objectToSave) {
        this.crud = crud;
        this.objectToDB = objectToSave;
    }

    void save() throws SQLException {
        checkAndSaveInnerManyToOne(objectToDB);
        List<Object> ids = checkAndSaveInnerManyToMany(objectToDB);
        RowToDB rowToDB = crud.cudBasics(objectToDB, QueryType.INSERT);
        setIdIfAutoIncrement(rowToDB);
        saveInnerOneToOneObjects();
        saveAssociatedTableForManyToMany(ids);
    }

    private void saveAssociatedTableForManyToMany(List<Object> ids) {
        Object idMain = getValueOfPK(objectToDB);
        Field[] fields = objectToDB.getClass().getDeclaredFields();
        List<Field> listFields = getAssociatedTableFields(objectToDB);
        Field[] fieldsFromList = listFields.toArray(new Field[listFields.size()]);

        insertIfAssociatedTableHolder(ids, idMain, fields);
        insertIfMapsIdHolder(ids, idMain, fieldsFromList);
    }

    private void insertIfMapsIdHolder(List<Object> ids, Object idMain, Field[] fieldsFromList) {
        for (Field field : fieldsFromList) {
            try {
                tablecreation.ManyToMany manyToMany = new ManyToManyConstructor(field).build();
                for (Object id : ids) {
                    String query = createQueryToAssociatedTable(idMain, id, manyToMany);
                    crud.getDataBase().executeUpdateQuery(query);
                }
            } catch (NoPrimaryKeyException e) {
                logger.error("No primary key");
            } catch (SQLException e) {
                logger.error(e, e.getCause());
                throw new QueryExecutionException("Could not update existing rows", e);
            }
        }
    }

    private void insertIfAssociatedTableHolder(List<Object> ids, Object idMain, Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToMany.class) && field.isAnnotationPresent(AssociatedTable.class)) {
                try {
                    tablecreation.ManyToMany manyToMany = new ManyToManyConstructor(field).build();
                    for (Object id : ids) {
                        String query = createQueryToAssociatedTable(id, idMain, manyToMany);
                        crud.getDataBase().executeUpdateQuery(query);
                    }
                } catch (NoPrimaryKeyException e) {
                    logger.error("No primary key, couldn`t save to DB");
                } catch (SQLException e) {
                    logger.error(e, e.getCause());
                    throw new QueryExecutionException("Could not update existing rows", e);
                }
            }
        }
    }

    private List<Field> getAssociatedTableFields(Object object) {
        List<Field> tables = new ArrayList<>();
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.isManyToManyPresentAndMappedByNotEmpty(field)) {
                String mappedBy = AnnotationUtils.getMappedByInManyToMany(field);
                Class classOfCollection = classGetTypeOfCollectionField(field);
                Field associatedTableField;
                try {
                    associatedTableField = classOfCollection.getDeclaredField(mappedBy);
                    tables.add(associatedTableField);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Annotation`s definition fail, check attribute mappedBy=\"\" in @ManyToMany");
                }
            }
        }
        return tables;
    }

    private void saveInnerOneToOneObjects() {
        try {
            List<Object> objectsOTOToSave = getAllObjects(objectToDB, OneToOne.class);
            for (Object o : objectsOTOToSave) {
                setIdToObject(o, getValueOfPK(objectToDB));
                crud.save(o);
            }
        } catch (IllegalAccessException | NoPrimaryKeyException e) {
            logger.error(e.getMessage());
        }
    }

    private void setIdIfAutoIncrement(RowToDB rowToDB) {
        if (rowToDB != null && rowToDB.isAutoIncrement()) {
            Object calculatedId = queryId(rowToDB, QueryType.SELECT_ID);
            try {
                setIdToObject(objectToDB, calculatedId);
            } catch (NoPrimaryKeyException e) {
                logger.error("Primary key is not found");
            }
        }
    }

    private List<Object> checkAndSaveInnerManyToMany(Object objectToDB) {
        List<Object> ids = null;
        try {
            List<Object> collectionsToSaveBefore = getAllObjects(objectToDB, ManyToMany.class);
            ids = new ArrayList<>();
            for (Object currentCollection : collectionsToSaveBefore) {
                if (currentCollection instanceof Collection) {
                    for (Object o : (Collection) currentCollection) {
                        ids.add(getValueOfPK(o));
                        if (checkIfNotInDB(o, getValueOfPK(o))) { 
                            logger.debug("Saving object -> " + o.toString());
                        crud.save(o);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return ids;
    }

    private void checkAndSaveInnerManyToOne(Object objectToDB) {
        try {
            List<Object> objectsToSaveBefore = getAllObjects(objectToDB, ManyToOne.class);
            for (Object o : objectsToSaveBefore) {
                logger.debug(o.toString());
                if (checkIfNotInDB(o, getValueOfPK(o)))
                    crud.save(o);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private Object queryId(RowToDB row, QueryType queryType) {
        String query = new QueryBuilderFactory().createQueryBuilder(row, queryType).buildQuery();
        try (Statement statement = crud.getDataBase().getStatement(query)) {
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
            logger.error(e, e.getCause());
        }
        return null;
    }

    private List<Object> getAllObjects(Object original, Class<? extends Annotation> annotation) throws IllegalAccessException {
        List<Object> allObjects = new ArrayList<>();
        Field[] fields = original.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (annotation.isAssignableFrom(OneToOne.class)) {
                if (field.isAnnotationPresent(annotation) && !field.isAnnotationPresent(MapsId.class)) {
                    field.setAccessible(true);
                    allObjects.add(field.get(original));
                }
            } else if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                allObjects.add(field.get(original));
            }
        }
        return allObjects;
    }

    private boolean checkIfNotInDB(Object someObject, Object id) {
        return crud.find(someObject.getClass(), id) == null;
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

    private String createQueryToAssociatedTable(Object idSide, Object idMain, tablecreation.ManyToMany mtm) {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue())
                //todo >_<
                .append(mtm.getAssociatedTableName()).append(" ");
        query.append("( ").append(mtm.getForeignKeyToOriginalTableName()).append(", ")
                .append(mtm.getForeignKeyToLinkedTableName()).append(" )").append(SQLStatements.VALUES.getValue()).append("( ");
        query.append(idMain.toString()).append(", ").append(idSide.toString()).append(" );");

        return query.toString();
    }

    private void setIdToObject(Object object, Object idToObject) throws NoPrimaryKeyException {
        Field[] fields = object.getClass().getDeclaredFields();
        String nameOfId = getNameOfPrimaryKey(fields);
        Field field;
        try {
            field = object.getClass().getDeclaredField(nameOfId);
            field.setAccessible(true);

            field.set(object, idToObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e);

        }
    }

    private String getNameOfPrimaryKey(Field[] fields) throws NoPrimaryKeyException {
        for (Field field : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                return field.getName();
            }
        }
        throw new NoPrimaryKeyException();
    }
}
