package tablecreation;

public class PrimaryKey {
    private Column primaryKey;

    public PrimaryKey(Column pk) {
        this.primaryKey = pk;
    }

    Column getColumnPrimaryKey() {
        return primaryKey;
    }
}
