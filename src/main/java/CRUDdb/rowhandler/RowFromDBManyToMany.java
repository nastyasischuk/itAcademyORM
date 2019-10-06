package CRUDdb.rowhandler;


public class RowFromDBManyToMany extends RowFromDB {
    private String manyToManyTableName;
    private String joinCoulmn;
    private String inverseColumn;

    public RowFromDBManyToMany(){
        super();
    }

    public String getManyToManyTableName() {
        return manyToManyTableName;
    }

    public void setManyToManyTableName(String manyToManyTableName) {
        this.manyToManyTableName = manyToManyTableName;
    }

    public String getJoinCoulmn() {
        return joinCoulmn;
    }

    public void setJoinCoulmn(String joinCoulmn) {
        this.joinCoulmn = joinCoulmn;
    }

    public String getInverseColumn() {
        return inverseColumn;
    }

    public void setInverseColumn(String inverseColumn) {
        this.inverseColumn = inverseColumn;
    }
}
