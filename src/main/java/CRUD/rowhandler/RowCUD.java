package CRUD.rowhandler;

import java.util.HashMap;
import java.util.Map;

public class RowCUD extends Row {
    private Map<String,String> nameOfColumnsAndValues;

    public RowCUD(String tableName, String idName, Object id) {
        super(tableName, idName, id);
        this.nameOfColumnsAndValues = new HashMap<>();

    }

    public RowCUD() {
        this.nameOfColumnsAndValues = new HashMap<>();
    }

    @Override
    public void setToMap(String name,String value) {
        nameOfColumnsAndValues.put(name,value);
    }
}
