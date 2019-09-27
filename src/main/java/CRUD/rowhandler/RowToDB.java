package CRUD.rowhandler;

import java.util.HashMap;
import java.util.Map;

public class RowToDB extends Row {
    private Map<String,String> nameOfColumnsAndValues;

    public RowToDB(String tableName) {
        super(tableName);
        this.nameOfColumnsAndValues = new HashMap<>();

    }

    public RowToDB() {
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
