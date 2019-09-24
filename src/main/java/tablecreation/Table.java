package tablecreation;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String tableName;
    private List<Column> columns;
    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys;
    private String checkConstraint;
    private List<Index> indexes;

    public Table(String tableName) {
        this.tableName = tableName;
        indexes = new ArrayList<>();
        foreignKeys = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void setCheckConstraint(String checkConstraint) {
        this.checkConstraint = checkConstraint;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setName(String name) {
        this.tableName = name;
    }

    public String getCheckConstraint() {
        return checkConstraint;
    }

    public List<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void addForeignKey(ForeignKey foreignKey) {
        foreignKeys.add(foreignKey);
    }

    public void setForeignKeys(List<ForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        if (primaryKey != null)
            throw new RuntimeException();//todo several primary keys then throw
        this.primaryKey = primaryKey;
    }

    public void setIndex(Index index) {
        this.indexes.add(index);
    }

    public List<Index> getIndexes() {
        return indexes;
    }
}
