package CRUD.rowhandler;

public abstract class Row {
    private String tableName;
    private String idName;
    private String idValue;

    Row() {
    }

    Row(String tableName) {
        this.tableName = tableName;

    }
    public String getTableName() {
        return tableName;
    }

    void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdName() {
        return idName;
    }

    void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdValue() {
        return idValue;
    }

    void setIdValue(String idValue) {
        this.idValue = idValue;
    }
}
