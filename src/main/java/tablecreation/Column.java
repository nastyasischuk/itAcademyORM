package tablecreation;

public class Column {
    private String name;
    private SQLTypes type;
    private String defaultValue;
    private boolean primaryKey;
    private boolean foreignKey;
    private boolean nullable = true;
    private boolean unique;
    private boolean autoincrement;

    public Column(String name, SQLTypes type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public SQLTypes getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setType(SQLTypes type) {
        this.type = type;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }
}
