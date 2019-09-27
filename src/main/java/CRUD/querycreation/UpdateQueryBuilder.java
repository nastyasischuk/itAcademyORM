package CRUD.querycreation;

import CRUD.rowhandler.Row;
import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class UpdateQueryBuilder extends QueryBuilder {
    //TODO change to right Class

    public UpdateQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNamesAndColumnValues = new StringBuilder();
        request.append(SQLStatements.UPDATE.getValue()).append(" ").append(row.getTableName());

        request.append(SQLStatements.SET).append(columnNamesAndColumnValues);
        request.append(SQLStatements.WHERE.getValue()).append(row.getIdName()).append(" ").append(row.getIdValue()).append(';');;
        return request.toString();
    }
}
