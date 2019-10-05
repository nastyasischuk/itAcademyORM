package CRUDdb.rowhandler;

import annotations.Column;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowToDB extends Row {
    private Field idField;
    private Map<String,String> nameOfColumnsAndValues;

    public RowToDB(String tableName) {
        super(tableName);
        this.nameOfColumnsAndValues = new LinkedHashMap<>();

    }
    public void setIdField(Field idField){
        this.idField = idField;
    }

    public void setToMap(String name,String value) {
        nameOfColumnsAndValues.put(name,value);
    }

    public Map<String, String> getMap() {
        return nameOfColumnsAndValues;
    }

    public boolean isAutoIncrement(){
        if (idField.isAnnotationPresent(Column.class))
            return idField.getAnnotation(Column.class).autoincrement();
        return false;
    }

    @Override
    public String toString() {
        return "RowToDB{" +
                "idField=" + idField +
                ", nameOfColumnsAndValues=" + nameOfColumnsAndValues +
                ", ai=" + isAutoIncrement() +
                '}';
    }
}
