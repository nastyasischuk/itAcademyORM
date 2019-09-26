package CRUD.rowhandler;

import CRUD.CRUD;

public abstract class Row {
    private String table_name;
    private String id_name;
    private Object id;

    public Row() {
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
