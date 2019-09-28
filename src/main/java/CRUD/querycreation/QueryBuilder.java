package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;

public abstract class QueryBuilder {
   RowToDB row;
    public QueryBuilder(RowToDB row){
        this.row = row;
    }
   public abstract String buildQuery();
}
