package CRUDdb.buildingObject;

import CRUDdb.rowhandler.RowFromDB;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.util.Map;

public class ObjectBuilderWithLinks extends ObjectBuilder {
    private static Logger logger = Logger.getLogger(ObjectBuilderWithLinks.class);
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
            Field field = classType.getDeclaredField(entry.getKey());
            field.setAccessible(true);
            if (entry.getKey().equals(mapping)) {
                if (field.getType() == objectToMappedBy.getClass())//if its oneToOne ManyTOne, for other associations skips because of recursion
                    field.set(objectToBuildFromDB, objectToMappedBy);
                continue;
            }

            String nameOfMethodInResultSetToGetValue = constructResultSetMethodName(entry.getValue());
            Object fieldValue = null;
            try {
                if (nameOfMethodInResultSetToGetValue == null) {
                    fieldValue = handleCasesWhenTypeIsNotSimple(field, entry.getKey());
                } else {
                    fieldValue = getValueFromResultSet(nameOfMethodInResultSetToGetValue, field.getName());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            field.set(objectToBuildFromDB, fieldValue);
        }
    }

}
