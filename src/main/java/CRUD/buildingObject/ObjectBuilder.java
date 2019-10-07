package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import annotations.*;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            if(row.getIdValue()==null){
                String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(row.getNameAndType().get(row.getIdName()));
                row.setIdValue( getValueFromResultSet(nameOfMethodInResultSetToGetValue, row.getIdName()).toString());
            }
            Field field=null;
            if(AnnotationUtils.getFieldByColemnName(classType,entry.getKey())!=null){
                field = classType.getDeclaredField(AnnotationUtils.getFieldByColemnName(classType,entry.getKey()).getName());
            }else
             field = classType.getDeclaredField(entry.getKey());

            field.setAccessible(true);
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue = null;
            try {
                if (nameOfMethodInResultSetToGetValue == null) {
                    fieldValue = handleCasesWhenTypeIsNotSimple(field, entry.getKey());
                } else {
                    fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue,entry.getKey());
                }
            } catch (Exception e) {
                logger.error(e,e.getCause());
            }

            field.set(objectToBuildFromDB, fieldValue);
            if (AnnotationUtils.isManyToOnePresent(field)) {
                listOfCollectionInObject.add(setToCollection(fieldValue));
            }
        }
        removeAllDuplicates(listOfCollectionInObject);
    }

    Object handleCasesWhenTypeIsNotSimple(Field field, String nameOfFieldToGet) {
        Object fieldValue = null;
        if (AnnotationUtils.isAnyOfAnnotationIsPresent(field, ForeignKey.class, MapsId.class, OneToOne.class, ManyToOne.class)) {
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(determinePrimaryKeyType(field));
            Object foreignKeyValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, nameOfFieldToGet);
            fieldValue = database.getCrud().find(field.getType(), foreignKeyValue);

        } else if (AnnotationUtils.isOneToManyPresent(field)) {
          fieldValue = handleOneTOMany(field);
        } else if (AnnotationUtils.isManyToManyPresent(field)) {
             fieldValue = handleManyTOMany(field);

        }
        return fieldValue;
    }
    private Collection<Object> handleOneTOMany(Field field){
        Collection<Object> foundObjectCollection = null;
        try {
            foundObjectCollection = database.getCrud().findCollection(field.getAnnotation(OneToMany.class).
                    typeOfReferencedObject(), row.getIdValue(), objectToBuildFromDB, field.getAnnotation(OneToMany.class).mappedBy());

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return foundObjectCollection;
    }
    private Collection<Object> handleManyTOMany(Field field){
        Collection<Object> foundObjectCollection = null;
        try {
            foundObjectCollection = database.getCrud().
                    findCollectionFoManyToMany(AnnotationUtils.classGetTypeOfCollectionField(field),
                            row.getIdValue(),
                            AnnotationUtils.getAssociatedTable(field));

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return foundObjectCollection;
    }

    private Class determinePrimaryKeyType(Field field) {
        Class classOfForeignKey = field.getType();
        Field[] fields = classOfForeignKey.getDeclaredFields();
        for (Field elOfFields : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(elOfFields)) {
                return elOfFields.getType();
            }
        }
        return null;
    }

    Object getValueFromResultSet(String nameOfMethod, String nameOfAttributeToGet) {
        Method method;
        Object valueOfObject = null;
        try {
            method = CachedRowSet.class.getMethod(nameOfMethod, String.class);
            valueOfObject = method.invoke(resultSet, nameOfAttributeToGet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e, e.getCause());
        }
        return valueOfObject;
    }

    private Collection<Object> setToCollection(Object fieldValue) {
        Field field = null;
        Collection collectionInManyToOne = null;
        for (Field personField : fieldValue.getClass().getDeclaredFields()) {
            if (personField.isAnnotationPresent(OneToMany.class))
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

    private void removeDuplicateInCollection(Collection<Object> collection) {
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

    private Object determinePrimaryKeyValue(Object objectInCollection) throws IllegalAccessException {
        Field[] fields = objectInCollection.getClass().getDeclaredFields();
        for (Field elOfFields : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(elOfFields)) {
                elOfFields.setAccessible(true);
                return elOfFields.get(objectInCollection);
            }
        }
        return null;
    }
}