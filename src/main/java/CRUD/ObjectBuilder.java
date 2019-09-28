package CRUD;

import CRUD.rowhandler.RowFromDB;
import annotations.*;
import tablecreation.DeterminatorOfType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Map;

public class ObjectBuilder {
    public static final String METHODNAMEFORINTEGER ="getInt";
    public static final String STARTOFMETHODRESULTSETTOGETVALUE ="get";
    private Object objectToBuildFromDB;
    private RowFromDB row;
    private ResultSet resultSet;
    private Class<?> classType;
    public ObjectBuilder(){

    }
    public ObjectBuilder(RowFromDB rowFromDB, ResultSet resultSet,Class<?> classType){
        this.resultSet = resultSet;
        this.row = rowFromDB;
        this.classType = classType;
        instantiateObject();

    }
    public Object buildObject() throws NoSuchFieldException,IllegalAccessException{
        setResultFromResultSet();
        return objectToBuildFromDB;
    }

    public void setResultFromResultSet() throws NoSuchFieldException,IllegalAccessException{
        for (Map.Entry<String,Class> entry: row.getNameAndType().entrySet()){
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue=null;
            if(nameOfMethodInResultSetToGetValue==null){//we could not find proper type then we check if it is foreign key

            }else {
                 fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, field.getName());
            }
            field.set(objectToBuildFromDB,fieldValue);//setting value that we got from resultSet
        }
    }
    private Object handleCasesWhenTypeIsNotSimple(Field field){
        Object fieldValue= null;
        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class) || field.isAnnotationPresent(OneToOne.class)
        || field.isAnnotationPresent(ManyToOne.class)){
           //todo getInteger of ResultSet
            fieldValue = null;//todo we have to call find for this method
        }else if(field.isAnnotationPresent(OneToMany.class)){
            //todo getInteger of ResultSet
            //fieldValue = find(field.getAnnotation(OneToMany.class).typeOfReferencedObject(),id,classType);
            //
        }
        return null;
    }






    public String constructResultSetMethodName(Class<?> typeOfresult){
        if(DeterminatorOfType.getSQLType(typeOfresult)==null){
            return null;
            //todo if it is not primitive or not a sql date then it has to be a foreign key or exception
        }
        if(typeOfresult==Integer.class){
            return METHODNAMEFORINTEGER;
        }
        String typeName = typeOfresult.getSimpleName();
        String s1 = typeName.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + typeName.substring(1);
        return STARTOFMETHODRESULTSETTOGETVALUE+nameCapitalized;
    }


    private Object getValueFromResultSet(String nameOfMethod,String nameOfAttributeToGet){
        Method method;
        Object valueOfObject = null;
        try {
             method = ResultSet.class.getMethod(nameOfMethod,String.class);
           valueOfObject =  method.invoke(resultSet,nameOfAttributeToGet);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            //todo add logger
        }
        return valueOfObject;
    }


    private void instantiateObject(){
        try{
            objectToBuildFromDB = classType.newInstance();
        }catch (Exception e){
            //todo add logger
        }
    }
}
