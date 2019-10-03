package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class DeleteQueryBuilder extends QueryBuilder {
    private RowToDB row;

    public DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.DELETE.getValue()).append(SQLStatements.FROM.getValue())
                .append(row.getTableName()).append(SQLStatements.WHERE.getValue());
        row.getMap().forEach((k, v) -> request.append(k + MarkingChars.space + v));
        request.append(MarkingChars.semicolon);
        return request.toString();
    }
}
