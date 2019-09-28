package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class InsertQueryBuilder extends QueryBuilder {
    public InsertQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        request.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue()).append(row.getTableName()).append(" ");
        //todo foreach keys
        request.append('(').append(columnNames).append(')').append(SQLStatements.VALUES.getValue()).append(" ");
        //todo foreach values
        request.append('(').append(columnValues).append(')').append(';');

        return null;
    }
}
