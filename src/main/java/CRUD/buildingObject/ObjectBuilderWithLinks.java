package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Map;

public class ObjectBuilderWithLinks extends ObjectBuilder {
    private String mapping;
    private Object objectToMappedBy;

    public ObjectBuilderWithLinks(RowFromDB rowFromDB, ResultSet resultSet, Class<?> classType, Object objectToMappedBy, String fieldThatMapped) {
        super(rowFromDB, resultSet, classType);
        this.mapping = fieldThatMapped;
        this.objectToMappedBy = objectToMappedBy;
    }
    @Override
    public void setResultFromResultSet() throws NoSuchFieldException,IllegalAccessException{
        for (Map.Entry<String,Class> entry: row.getNameAndType().entrySet()){
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            if(entry.getKey().equals(mapping)){
                field.set(objectToBuildFromDB,objectToMappedBy);
                continue;
            }
            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue=null;
            if(nameOfMethodInResultSetToGetValue==null){//we could not find proper type then we check if it is foreign key

            }else {
                fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, field.getName());
            }
            field.set(objectToBuildFromDB,fieldValue);//setting value that we got from resultSet
        }
    }
}
