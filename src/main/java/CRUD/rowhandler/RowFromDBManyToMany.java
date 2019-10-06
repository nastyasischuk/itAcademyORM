package CRUD.rowhandler;

public class RowFromDBManyToMany extends RowFromDB {
    private String manyToManyTableName;
    private String joinColumn;
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
        return joinColumn;
    }

    public void setJoinCoulmn(String joinCoulmn) {
        this.joinColumn = joinCoulmn;
    }

    public String getInverseColumn() {
        return inverseColumn;
    }

    public void setInverseColumn(String inverseColumn) {
        this.inverseColumn = inverseColumn;
    }
}
