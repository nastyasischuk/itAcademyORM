package CRUD.querycreation;

import CRUD.rowhandler.Row;
import tablecreation.SQLStatements;

public class InsertQueryBuilder extends QueryBuilder {
    private Row row;//TODO change to right Class

    public InsertQueryBuilder(Row row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        request.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue()).append(row.getTable_name()).append(" ");
        //todo foreach keys
        request.append('(').append(columnNames).append(')').append(SQLStatements.VALUES.getValue()).append(" ");
        //todo foreach values
        request.append('(').append(columnValues).append(')').append(';');

        return null;
    }
}
