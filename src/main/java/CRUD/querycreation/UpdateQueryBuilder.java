package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;

import tablecreation.SQLStatements;


public class UpdateQueryBuilder extends QueryBuilder {
    private Row row;//TODO change to right Class

    public UpdateQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNamesAndColumnValues = new StringBuilder();
        request.append(SQLStatements.UPDATE.getValue()).append(" ").append(row.getTable_name());
        //TODO for(keys : MAP)
        request.append(SQLStatements.SET).append(columnNamesAndColumnValues);
        request.append(SQLStatements.WHERE.getValue()).append(row.getId_name()).append(" ").append(row.getId()).append(';');;
        return request.toString();
    }
}
