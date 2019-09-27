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

    public Map<String, String> getNameOfColumnsAndValues() {
        return nameOfColumnsAndValues;
    }

    public void setNameOfColumnsAndValues(Map<String, String> nameOfColumnsAndValues) {
        this.nameOfColumnsAndValues = nameOfColumnsAndValues;
    }
}
