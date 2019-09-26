package CRUD.rowhandler;

import java.util.HashMap;
import java.util.Map;

public class RowCUD extends Row {
    private Map<String,String> nameOfColumnsAndValues;

    public RowCUD(String tableName) {
        super(tableName);
        this.nameOfColumnsAndValues = new HashMap<>();

    }

    public RowCUD() {
        this.nameOfColumnsAndValues = new HashMap<>();
    }

    @Override
    public void setToMap(String name,String value) {
        nameOfColumnsAndValues.put(name,value);
    }

    @Override
    public Map<String, String> getMap() {
        return nameOfColumnsAndValues;
    }
}
