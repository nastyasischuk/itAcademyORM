package CRUD.rowhandler;

import java.util.Map;

public class RowFromDB extends Row {

    private Map<String, Class> nameAndType;

    public RowFromDB(Map<String, Class> nameAndType) {
        this.nameAndType = nameAndType;
    }

    public RowFromDB(String tableName, Map<String, Class> nameAndType) {
        super(tableName);
        this.nameAndType = nameAndType;
    }

    public RowFromDB() {
        super();
    }

    public Map<String, Class> getNameAndType() {
        return nameAndType;
    }

    public void setNameAndType(Map<String, Class> nameAndType) {
        this.nameAndType = nameAndType;
    }
}
