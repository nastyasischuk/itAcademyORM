package CRUD.querycreation;

import CRUD.rowhandler.RowFromDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDB {
    RowFromDB rowFromDB;

    public QueryBuilderFromDB(RowFromDB rowFromDB) {
        this.rowFromDB = rowFromDB;
    }
    public String buildQuery(){
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.SELECT.getValue()).append(" * ").append(rowFromDB.getTableName());
        return request.toString();
    }
}
