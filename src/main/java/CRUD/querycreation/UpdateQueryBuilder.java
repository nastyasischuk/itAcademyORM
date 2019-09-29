package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;

import tablecreation.SQLStatements;

import java.util.Map;


public class UpdateQueryBuilder extends QueryBuilder {


    public UpdateQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNamesAndColumnValues = new StringBuilder();
        request.append(SQLStatements.UPDATE.getValue()).append(" ").append(row.getTableName());
        for(Map.Entry<String,String> entry: row.getMap().entrySet()) {
            request.append(SQLStatements.SET).append(columnNamesAndColumnValues);
            request.append(SQLStatements.WHERE.getValue()).append(entry.getKey()).append(" ").append(entry.getValue()).append(';');

        }
        return request.toString();
    }
}
