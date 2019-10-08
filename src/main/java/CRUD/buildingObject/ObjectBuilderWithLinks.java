package CRUD.buildingObject;

import CRUD.rowhandler.RowFromDB;
import connection.DataBase;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.util.Map;

public class ObjectBuilderWithLinks extends ObjectBuilder {
    private String mapping;
    private Object objectToMappedBy;

    public ObjectBuilderWithLinks(RowFromDB rowFromDB, CachedRowSet resultSet, Class<?> classType, Object objectToMappedBy, String fieldThatMapped, DataBase db) {
        super(rowFromDB, resultSet, classType, db);
        this.mapping = fieldThatMapped;
        this.objectToMappedBy = objectToMappedBy;
    }

    @Override
    public void setResultFromResultSet() throws NoSuchFieldException, IllegalAccessException {
        for (Map.Entry<String, Class> entry : row.getNameAndType().entrySet()) {
          setValueToEveryFieldOfObject(entry);
        }
    }

    private void setValueToEveryFieldOfObject(Map.Entry<String, Class> entry)throws NoSuchFieldException, IllegalAccessException{
        Field field= getFieldOfObject(entry);
        field.setAccessible(true);
        if (entry.getKey().equals(mapping)) {
            linkObjectsForManyToOneOneToOne(field);
            return;
        }
        String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
        Object fieldValue = getValueOfField(entry, field, nameOfMethodInResultSetToGetValue);
        field.set(objectToBuildFromDB, fieldValue);
    }
    private void linkObjectsForManyToOneOneToOne(Field field) throws IllegalAccessException {
        if (field.getType() == objectToMappedBy.getClass())
            field.set(objectToBuildFromDB, objectToMappedBy);
    }
}
