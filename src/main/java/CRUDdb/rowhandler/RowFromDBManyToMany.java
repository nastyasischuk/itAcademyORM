package CRUDdb.rowhandler;


public class RowFromDBManyToMany extends RowFromDB {
    private String manyToManyTableName;
    private String join_coulmn;
    private String inverse_column;

    public RowFromDBManyToMany(){
        super();
    }

    public String getManyToManyTableName() {
        return manyToManyTableName;
    }

    public void setManyToManyTableName(String manyToManyTableName) {
        this.manyToManyTableName = manyToManyTableName;
    }

    public String getJoin_coulmn() {
        return join_coulmn;
    }

    public void setJoin_coulmn(String join_coulmn) {
        this.join_coulmn = join_coulmn;
    }

    public String getInverse_column() {
        return inverse_column;
    }

    public void setInverse_column(String inverse_column) {
        this.inverse_column = inverse_column;
    }
}
