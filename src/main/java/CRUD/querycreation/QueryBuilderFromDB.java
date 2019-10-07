package CRUD.querycreation;

import CRUD.rowhandler.RowFromDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDB {
    private RowFromDB rowFromDB;

    QueryBuilderFromDB(RowFromDB rowFromDB) {
        this.rowFromDB = rowFromDB;
    }

    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(" * ").append(SQLStatements.FROM.getValue())
                .append(rowFromDB.getTableName()).append(SQLStatements.WHERE.getValue()).append(rowFromDB.getIdName())
                .append("=").append(rowFromDB.getIdValue());
        return query.toString();
    }
}
