package CRUD.rowhandler;

import annotations.AnnotationUtils;
import annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowToDB extends Row {
    private Field idField;
    private Map<String, String> nameOfColumnsAndValues;

    public RowToDB(String tableName) {
        super(tableName);
        this.nameOfColumnsAndValues = new LinkedHashMap<>();
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public void setToMap(String name, String value) {
        nameOfColumnsAndValues.put(name, value);
    }

    public Map<String, String> getMap() {
        return nameOfColumnsAndValues;
    }

    public boolean isAutoIncrement() {
        if (AnnotationUtils.isPrimaryKeyPresent(idField))
            return idField.getAnnotation(PrimaryKey.class).autoincrement();
        return false;
    }
}
