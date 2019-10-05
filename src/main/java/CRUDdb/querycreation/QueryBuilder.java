package CRUDdb.querycreation;


import CRUDdb.rowhandler.RowToDB;

public abstract class QueryBuilder {
    RowToDB row;

    public QueryBuilder(RowToDB row){
        this.row = row;
    }

    public abstract String buildQuery();
}
