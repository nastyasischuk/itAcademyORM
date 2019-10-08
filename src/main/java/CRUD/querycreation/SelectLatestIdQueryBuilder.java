package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;
import customQuery.MarkingChars;
import tablecreation.SQLStatements;

public class SelectLatestIdQueryBuilder extends QueryBuilder {
    SelectLatestIdQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(SQLStatements.MAX).append(MarkingChars.OPEN_BRACKET)
                .append(row.getIdName()).append(MarkingChars.CLOSED_BRACKET).append(SQLStatements.FROM.getValue())
                .append(row.getTableName());
        return query.toString();
    }
}
