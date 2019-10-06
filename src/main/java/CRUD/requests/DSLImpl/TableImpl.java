package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class TableImpl implements Table {
    private String name;

    TableImpl(String tableName) {
        this.name = tableName;
    }

    @Override
    public String getName() {
        return name;
    }
}
