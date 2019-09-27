package CRUD.querycreation;

import CRUD.rowhandler.Row;

public abstract class QueryBuilder {
   Row row;
    public QueryBuilder(Row row){
        this.row = row;
    }
   public abstract String buildQuery();
}
