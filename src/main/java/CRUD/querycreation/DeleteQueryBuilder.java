package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;

public class DeleteQueryBuilder extends QueryBuilder{
    public DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.DELETE.getValue()).append(SQLStatements.FROM.getValue()).append(row.getTable_name()).append(SQLStatements.WHERE.getValue());
        //TODO forEach map of row
        request.append(';');
        return request.toString();
    }
}
