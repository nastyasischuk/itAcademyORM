package tablecreation;

import java.util.List;

public class Table {
    private String name;
    private List<Column> columns;
    //TODO PK
    //TODO FK
    public Table(String name){
        this.name = name;
    }
    public void addColumn(Column column){
        //TODO determine whether fk or pk;
    }
}
