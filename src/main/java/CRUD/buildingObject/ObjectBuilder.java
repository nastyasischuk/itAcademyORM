package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import annotations.*;

import connection.DataBaseImplementation;

import org.apache.log4j.Logger;

import tablecreation.DeterminatorOfType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Map;

public class ObjectBuilder {
    private static Logger logger = Logger.getLogger(ObjectBuilder.class);
    public static final String METHODNAMEFORINTEGER ="getInt";
    public static final String STARTOFMETHODRESULTSETTOGETVALUE ="get";
    protected DataBaseImplementation database;
    protected Object objectToBuildFromDB;
    protected RowFromDB row;
    protected ResultSet resultSet;
    protected Class<?> classType;
    public ObjectBuilder(){

    }
    public ObjectBuilder(RowFromDB rowFromDB, ResultSet resultSet,Class<?> classType,DataBaseImplementation db){
        this.resultSet = resultSet;
        this.row = rowFromDB;
        this.classType = classType;
        this.database= db;
        instantiateObject();

    }
    public Object buildObject() throws NoSuchFieldException,IllegalAccessException,NoSuchMethodException,InvocationTargetException{
        setResultFromResultSet();
        return objectToBuildFromDB;
    }

    public void setResultFromResultSet() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Map.Entry<String,Class> entry: row.getNameAndType().entrySet()){
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue=null;
            try{
            if(nameOfMethodInResultSetToGetValue==null){
                fieldValue = handleCasesWhenTypeIsNotSimple(field,entry.getKey());
            }else {
                 fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, entry.getKey());

            }
            }catch (Exception e){

            }
            field.set(objectToBuildFromDB,fieldValue);
        }
    }
    protected Object handleCasesWhenTypeIsNotSimple(Field field,String nameOfFieldToGet) throws IllegalAccessException{
        Object fieldValue= null;
        Object foreignKeyValue = getValueFromResultSet(METHODNAMEFORINTEGER,nameOfFieldToGet);
        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class) || field.isAnnotationPresent(OneToOne.class)
        || field.isAnnotationPresent(ManyToOne.class)){
            fieldValue = database.getCrud().find(field.getType(),foreignKeyValue);
        }else if(field.isAnnotationPresent(OneToMany.class)){
            fieldValue = database.getCrud().findCollection(field.getAnnotation(OneToMany.class).typeOfReferencedObject(),row.getIdValue(),objectToBuildFromDB,field.getAnnotation(OneToMany.class).mappedBy());

        }else return null;//todo
        return fieldValue;
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
             method = ResultSet.class.getMethod(nameOfMethod,String.class);
           valueOfObject =  method.invoke(resultSet,nameOfAttributeToGet);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.error(e.getMessage());
        }
        return valueOfObject;
    }


    private void instantiateObject(){
        try{
            objectToBuildFromDB = classType.newInstance();
        }catch (Exception e){
            logger.error(e.getMessage());
            //todo add logger
        }
    }
}
