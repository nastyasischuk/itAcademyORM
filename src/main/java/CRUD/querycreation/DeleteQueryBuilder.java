package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;
import customQuery.MarkingChars;
import tablecreation.SQLStatements;

public class DeleteQueryBuilder extends QueryBuilder {

    DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.DELETE.getValue()).append(SQLStatements.FROM.getValue()).append(row.getTableName())
                .append(SQLStatements.WHERE.getValue()).append(row.getIdName()).append(" = ").append(row.getIdValue());
        query.append(MarkingChars.semicolon);
        return query.toString();
    }
}
