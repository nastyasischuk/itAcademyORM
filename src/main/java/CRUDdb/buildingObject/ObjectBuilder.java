package CRUDdb.buildingObject;

import CRUDdb.rowhandler.RowFromDB;
import annotations.*;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectBuilder extends ObjectSimpleBuilding {
    private static Logger logger = Logger.getLogger(ObjectBuilder.class);
    protected RowFromDB row;
    protected DataBase database;

    public ObjectBuilder() {

    }

    public ObjectBuilder(RowFromDB rowFromDB, CachedRowSet resultSet, Class<?> classType, DataBase db) {
        super(resultSet, classType);
        this.database = db;
        this.row = rowFromDB;
    }

    public Object buildObject() throws NoSuchFieldException, IllegalAccessException {
        setResultFromResultSet();
        return objectToBuildFromDB;
    }

    public void setResultFromResultSet() throws NoSuchFieldException, IllegalAccessException {
        List<Collection<Object>> listOfCollectionInObject = new ArrayList<>();
        for (Map.Entry<String, Class> entry : row.getNameAndType().entrySet()) {
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue = null;
            try {
                if (nameOfMethodInResultSetToGetValue == null) {
                    fieldValue = handleCasesWhenTypeIsNotSimple(field, entry.getKey());
                } else {
                    fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, entry.getKey());
                }
            } catch (Exception e) {
                logger.error(e,e.getCause());
            }
            field.set(objectToBuildFromDB, fieldValue);
            if (field.isAnnotationPresent(ManyToOne.class)) {
                listOfCollectionInObject.add(setToCollection(fieldValue));
            }
        }
        removeAllDuplicates(listOfCollectionInObject);
    }

    protected Object handleCasesWhenTypeIsNotSimple(Field field, String nameOfFieldToGet) {
        Object fieldValue = null;
        if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class) || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(ManyToOne.class)) {
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(determinePrimaryKeyType(field));
            Object foreignKeyValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, nameOfFieldToGet);
            fieldValue = database.getCrud().find(field.getType(), foreignKeyValue);
        } else if (field.isAnnotationPresent(OneToMany.class)) {
            try {
                Collection<Object> col = database.getCrud().findCollection(field.getAnnotation(OneToMany.class).
                        typeOfReferencedObject(), row.getIdValue(), objectToBuildFromDB, field.getAnnotation(OneToMany.class).mappedBy());
                fieldValue = col;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            try {
                Collection<Object> col = database.getCrud().
                        findCollectionFoManyToMany(AnnotationUtils.classGetTypeOfCollectionField(field),
                                row.getIdValue(),
                                AnnotationUtils.getAssociatedTable(field));
                fieldValue = col;
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
        return fieldValue;
    }

    protected Class determinePrimaryKeyType(Field field) {
        Class classOfForeignKey = field.getType();
        Field[] fields = classOfForeignKey.getDeclaredFields();
        for (Field elOfFields : fields) {
            if (elOfFields.isAnnotationPresent(PrimaryKey.class)) {
                return elOfFields.getType();
            }
        }
        return null;
    }

    protected Object getValueFromResultSet(String nameOfMethod, String nameOfAttributeToGet) {
        Method method;
        Object valueOfObject = null;
        try {
            method = CachedRowSet.class.getMethod(nameOfMethod, String.class);
            valueOfObject = method.invoke(resultSet, nameOfAttributeToGet);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            logger.error(e, e.getCause());
        } catch (InvocationTargetException e) {
            logger.error(e,e.getCause());
        }
        return valueOfObject;
    }

    protected Collection<Object> setToCollection(Object fieldValue) {
        Field field = null;
        Collection collectionInManyToOne = null;
        for (Field personField : fieldValue.getClass().getDeclaredFields()) {
            if (personField.isAnnotationPresent(OneToMany.class)
            )
                field = personField;
        }
        try {
            Method method = field.getType().getMethod("add", Object.class);
            field.setAccessible(true);
            collectionInManyToOne = (Collection<Object>) field.get(fieldValue);
            method.invoke(collectionInManyToOne, classType.cast(objectToBuildFromDB));
        } catch (Exception e) {
            logger.error(e);
        }
        return collectionInManyToOne;
    }

    public void removeAllDuplicates(List<Collection<Object>> toRemoveDublicates) {
        for (Collection<Object> collection : toRemoveDublicates) {
            removeDuplicateInCollection(collection);
        }
    }

    public void removeDuplicateInCollection(Collection<Object> collection) {
        Object objectToRemove = null;
        for (Object element : collection) {
            try {
                if (determinePrimaryKeyValue(element).equals(determinePrimaryKeyValue(this.objectToBuildFromDB)) && this.objectToBuildFromDB != element) {
                    objectToRemove = element; }
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }
        collection.remove(objectToRemove);
    }

    protected Object determinePrimaryKeyValue(Object objectInCollection) throws IllegalAccessException {
        Field[] fields = objectInCollection.getClass().getDeclaredFields();
        for (Field elOfFields : fields) {
            if (elOfFields.isAnnotationPresent(PrimaryKey.class)) {
                elOfFields.setAccessible(true);
                return elOfFields.get(objectInCollection);
            }
        }
        return null;
    }
}