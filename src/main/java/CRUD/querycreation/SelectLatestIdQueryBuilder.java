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
        query.append(SQLStatements.SELECT.getValue()).append(SQLStatements.MAX).append(MarkingChars.openBracket)
                .append(row.getIdName()).append(MarkingChars.closedBracket).append(SQLStatements.FROM.getValue())
                .append(row.getTableName());
        return query.toString();
    }
}
