package CRUD.rowhandler;

import java.util.Map;

public abstract class Row {
    private String tableName;
    private String idName;
    private String id;

    public Row() {
    }

    public Row(String tableName, String idName, String id) {
        this.tableName = tableName;
        this.idName = idName;
        this.id = id;
    }

    public abstract void setToMap(String name,String value);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
