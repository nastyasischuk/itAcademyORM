package CRUDdb.querycreation;

import CRUDdb.requests.MarkingChars;
import CRUDdb.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class SelectLatestIdQueryBuilder extends QueryBuilder {
    public SelectLatestIdQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.SELECT.getValue()).append(SQLStatements.MAX)
                .append(MarkingChars.openBracket).append(row.getIdName()).append(MarkingChars.closedBracket)
                .append(SQLStatements.FROM.getValue()).append(row.getTableName());
        return request.toString();
    }
}
