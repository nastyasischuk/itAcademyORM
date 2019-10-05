package CRUDdb.rowhandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class RowFromDB extends Row {

    private Map<String, Class> nameAndType;


    public RowFromDB() {
        super();
        this.nameAndType = new LinkedHashMap<>();
    }

    public Map<String, Class> getNameAndType() {
        return nameAndType;
    }

    public void setNameAndType(Map<String, Class> nameAndType) {
        this.nameAndType = nameAndType;
    }
}
