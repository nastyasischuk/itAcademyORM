package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import annotations.*;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;
import tablecreation.DeterminatorOfType;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class ObjectBuilder {
    public static Logger logger = Logger.getLogger(ObjectBuilder.class);
    public static final String METHODNAMEFORINTEGER ="getInt";
    public static final String STARTOFMETHODRESULTSETTOGETVALUE ="get";
    protected DataBaseImplementation database;
    protected Object objectToBuildFromDB;
    protected RowFromDB row;
    protected CachedRowSet resultSet;
    protected Class<?> classType;
    public ObjectBuilder(){

    }
    public ObjectBuilder(RowFromDB rowFromDB, CachedRowSet resultSet,Class<?> classType,DataBaseImplementation db){
        this.resultSet = resultSet;
        this.row = rowFromDB;
        this.classType = classType;
        this.database= db;
        this.objectToBuildFromDB = instantiateObject();
        logger.info(objectToBuildFromDB);
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
            logger.info(e.getMessage());
            }
            field.set(objectToBuildFromDB,fieldValue);
            if(field.isAnnotationPresent(ManyToOne.class)){
                setToCollection(fieldValue);
            }
        }
    }
    protected Object handleCasesWhenTypeIsNotSimple(Field field,String nameOfFieldToGet) {
        Object fieldValue= null;

        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class) || field.isAnnotationPresent(OneToOne.class)
        || field.isAnnotationPresent(ManyToOne.class)){
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(determinePrimaryKeyType(field));
            Object foreignKeyValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue,nameOfFieldToGet);//todo determine type of foreign key
            fieldValue = database.getCrud().find(field.getType(),foreignKeyValue);

        }else if(field.isAnnotationPresent(OneToMany.class)){
            try {
               Collection<Object> col =  database.getCrud().findCollection(field.getAnnotation(OneToMany.class).typeOfReferencedObject(), row.getIdValue(), objectToBuildFromDB, field.getAnnotation(OneToMany.class).mappedBy());


                fieldValue = col;
            }catch (Exception e){logger.info(e.getMessage());}
        }
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
             method = CachedRowSet.class.getMethod(nameOfMethod,String.class);
           valueOfObject =  method.invoke(resultSet,nameOfAttributeToGet);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.error(e);
        }
        return valueOfObject;
    }


    private Object instantiateObject(){
        Object toInst =null;
        try{
            Class aClass = Class.forName(classType.getName());
            toInst = aClass.newInstance();
            logger.info(objectToBuildFromDB.toString());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return toInst;
    }
    private Class determinePrimaryKeyType(Field field) {
        Class classOfForeignKey = field.getType();
        Field[] fields = classOfForeignKey.getDeclaredFields();
        for(Field elOfFields:fields){
            if(elOfFields.isAnnotationPresent(PrimaryKey.class)){
                return elOfFields.getType();
            }
        }
        return null;
    }
    private void setToCollection(Object fieldValue) {
        Field field = null;
        for(Field personField:fieldValue.getClass().getDeclaredFields()){
            if(personField.isAnnotationPresent(OneToMany.class )
                  )
                field=personField;
        }
        try {
            logger.info(field.getType());
           Method method = field.getType().getMethod("add",Object.class);
            logger.info(objectToBuildFromDB.toString());
            logger.info(classType.cast(objectToBuildFromDB));
            field.setAccessible(true);
            logger.info(fieldValue);
            method.invoke(field.get(fieldValue),classType.cast(objectToBuildFromDB) );
            logger.info(fieldValue);
        }catch(Exception e){
            logger.error(e);
        }
    }
}
