package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import connection.DataBaseImplementation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Map;

public class ObjectBuilderWithLinks extends ObjectBuilder {
    private String mapping;
    private Object objectToMappedBy;

    public ObjectBuilderWithLinks(RowFromDB rowFromDB, ResultSet resultSet, Class<?> classType, Object objectToMappedBy, String fieldThatMapped, DataBaseImplementation db) {
        super(rowFromDB, resultSet, classType,db);
        this.mapping = fieldThatMapped;
        this.objectToMappedBy = objectToMappedBy;
    }
    @Override
    public void setResultFromResultSet() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Map.Entry<String,Class> entry: row.getNameAndType().entrySet()){
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            if(entry.getKey().equals(mapping)){
            setDeterminingIfCollectionOrType(field);
                continue;
            }
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue=null;
            try {
                if (nameOfMethodInResultSetToGetValue == null) {
                    fieldValue = handleCasesWhenTypeIsNotSimple(field, entry.getKey());
                } else {
                    fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, field.getName());
                }
            }catch (Exception e){
                //todo add logger
            }
            field.set(objectToBuildFromDB,fieldValue);
        }
    }
    public void setDeterminingIfCollectionOrType(Field field) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        if(field.getType() == objectToMappedBy.getClass())
            field.set(objectToBuildFromDB,objectToMappedBy);
        else{
            Method methodToInsertIntoCollection = field.getType().getMethod("add",objectToMappedBy.getClass());
            methodToInsertIntoCollection.invoke(field.get(objectToBuildFromDB),objectToMappedBy);
        }
    }
}
