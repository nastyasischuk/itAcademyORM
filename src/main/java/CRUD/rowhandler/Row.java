package CRUD.rowhandler;

import java.util.Map;

public abstract class Row {
    private String tableName;
    private String idName;
    private String idValue;

    public Row() {
    }

    public Row(String tableName) {
        this.tableName = tableName;

    }

    public abstract void setToMap(String name,String value);
    public abstract Map<String,String> getMap();
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }
}
