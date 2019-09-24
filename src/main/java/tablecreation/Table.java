package tablecreation;

import java.util.List;

public class Table {
    private String name;
    private List<Column> columns;
    PrimaryKey primaryKey;
    List<ForeignKey> foreignKeys;

    public Table(String name){
        this.name = name;

    }

    public void addForeignKey(ForeignKey foreignKey){
        foreignKeys.add(foreignKey);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        if(primaryKey!=null)
            throw new RuntimeException();//todo several primary keys then throw
        this.primaryKey = primaryKey;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
