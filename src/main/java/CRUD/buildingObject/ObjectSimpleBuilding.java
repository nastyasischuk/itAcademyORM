package CRUD.buildingObject;

import annotations.AnnotationUtils;
import annotations.PrimaryKey;
import org.apache.log4j.Logger;
import tablecreation.DeterminatorOfType;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectSimpleBuilding {
    public static Logger logger = Logger.getLogger(ObjectSimpleBuilding.class);
    public static final String METHODNAMEFORINTEGER ="getInt";
    public static final String STARTOFMETHODRESULTSETTOGETVALUE ="get";

    protected Object objectToBuildFromDB;
    protected CachedRowSet resultSet;
    protected Class<?> classType;
    public ObjectSimpleBuilding(){

    }
    public ObjectSimpleBuilding(CachedRowSet resultSet, Class<?> classType){
        this.resultSet = resultSet;
        this.classType = classType;
        this.objectToBuildFromDB = instantiateObject();
    }
    public Object buildObject() throws NoSuchFieldException,IllegalAccessException,NoSuchMethodException,InvocationTargetException{
        String namePK =getPKName();
        Field pkField = primaryKeyField();
        setPK(pkField,getPKValueFromResultSet(namePK,pkField.getType()));
        return objectToBuildFromDB;
    }
    protected Object instantiateObject(){
        Object toInst =null;
        try{
            Class aClass = Class.forName(classType.getName());
            toInst = aClass.newInstance();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return toInst;
    }
    public String constructResultSetMethodName(Class<?> typeOfresult){
        if(DeterminatorOfType.getSQLType(typeOfresult)==null){
            return null;
        }
        if(typeOfresult==Integer.class){
            return METHODNAMEFORINTEGER;
        }
        String typeName = typeOfresult.getSimpleName();
        String s1 = typeName.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + typeName.substring(1);
        return STARTOFMETHODRESULTSETTOGETVALUE+nameCapitalized;
    }

    protected Object getValueFromResultSet(String nameOfMethod,String nameOfAttributeToGet){
        Method method;
        Object valueOfObject = null;
        try {
            method = CachedRowSet.class.getMethod(nameOfMethod,String.class);
            valueOfObject =  method.invoke(resultSet,nameOfAttributeToGet);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.error(e);
        }
        return valueOfObject;
    }


    private Field primaryKeyField(){
        Field[] fields = classType.getDeclaredFields();
        Field primaryKeyField = null;
        for(Field field:fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                primaryKeyField = field;
            }
        }
        return primaryKeyField;
    }
    private String getPKName(){
        Field primaryKeyField = null;
        Field[] fields = classType.getDeclaredFields();
        for(Field field:fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                primaryKeyField = field;
            }
        }
        String name;
        if(AnnotationUtils.isColumnPresentAndNotEmpty(primaryKeyField)){
            name = AnnotationUtils.getColumnName(primaryKeyField);
        }else name = primaryKeyField.getName();
        return name;
    }
    private Object getPKValueFromResultSet(String name, Class<?> pkType){
        String methodName= constructResultSetMethodName(Integer.class);
        return getValueFromResultSet(methodName,name);
    }
    private void setPK(Field pkField,Object pkValue){
        pkField.setAccessible(true);
        try {
            pkField.set(objectToBuildFromDB, pkValue);
        }catch (Exception e){
            logger.error(e,e.getCause());
        }
    }

}
