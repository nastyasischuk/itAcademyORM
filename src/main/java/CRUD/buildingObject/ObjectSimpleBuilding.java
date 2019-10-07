package CRUD.buildingObject;
import annotations.PrimaryKey;
import annotations.AnnotationUtils;

import org.apache.log4j.Logger;
import tablecreation.DeterminatorOfType;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectSimpleBuilding {
    public static Logger logger = Logger.getLogger(ObjectSimpleBuilding.class);

    public static final String METHOD_NAME_FOR_INTEGER = "getInt";
    public static final String START_OF_METHOD_RESULTSET_TO_GET_VALUE = "get";

    protected Object objectToBuildFromDB;
    protected CachedRowSet resultSet;
    protected Class<?> classType;

    public ObjectSimpleBuilding() {
    }

    public ObjectSimpleBuilding(CachedRowSet resultSet, Class<?> classType) {
        this.resultSet = resultSet;
        this.classType = classType;
        this.objectToBuildFromDB = instantiateObject();
    }

    public Object buildObject() throws NoSuchFieldException, IllegalAccessException {
        Field pkField = primaryKeyField();
        setPK(pkField, getPKValueFromResultSet(pkField.getType()));
        return objectToBuildFromDB;
    }

    private Object instantiateObject() {
        Object objectToInstantiate = null;
        try {
            objectToInstantiate = classType.newInstance();
        } catch (Exception e) {
            logger.error(e, e.getCause());
        }
        return objectToInstantiate;
    }

    public String constructResultSetMethodName(Class<?> typeOfresult) {
        if (DeterminatorOfType.getSQLType(typeOfresult) == null) {
            return null;
        }
        if (typeOfresult == Integer.class) {
            return METHOD_NAME_FOR_INTEGER;
        }
        String typeName = typeOfresult.getSimpleName();
        String s1 = typeName.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + typeName.substring(1);
        return START_OF_METHOD_RESULTSET_TO_GET_VALUE + nameCapitalized;
    }

    private Object getValueFromResultSet(String nameOfMethod) {
        Method method;
        Object valueOfObject = null;
        try {
            method = CachedRowSet.class.getMethod(nameOfMethod, int.class);
            valueOfObject = method.invoke(resultSet, 1);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            logger.error(e, e.getCause());
        } catch (InvocationTargetException e) {
            logger.error(e,e.getCause());
        }
        return valueOfObject;
    }


    private Field primaryKeyField() {
        Field[] fields = classType.getDeclaredFields();
        Field primaryKeyField = null;
        for (Field field : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                primaryKeyField = field;
            }
        }
        return primaryKeyField;
    }

    private Object getPKValueFromResultSet(Class<?> pkType) {
        String methodName = constructResultSetMethodName(pkType);
        return getValueFromResultSet(methodName);
    }

    private void setPK(Field pkField, Object pkValue) {
        pkField.setAccessible(true);
        try {
            pkField.set(objectToBuildFromDB, pkValue);
        } catch (Exception e) {
            logger.error(e, e.getCause());
        }
    }
}
