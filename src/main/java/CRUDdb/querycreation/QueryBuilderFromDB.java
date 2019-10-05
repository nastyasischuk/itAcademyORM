package CRUDdb.querycreation;

import CRUDdb.rowhandler.RowFromDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDB {
   private RowFromDB rowFromDB;

    public QueryBuilderFromDB(RowFromDB rowFromDB) {
        this.rowFromDB = rowFromDB;
    }
    public String buildQuery(){
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.SELECT.getValue()).append(" * ").append(SQLStatements.FROM.getValue()).append(rowFromDB.getTableName()).
                append(SQLStatements.WHERE.getValue()).append(rowFromDB.getIdName()).append("=").append(rowFromDB.getIdValue());
        return request.toString();
    }
}
