package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class SelectLatestIdQueryBuilder extends QueryBuilder {
    public SelectLatestIdQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.SELECT.getValue()).append(SQLStatements.MAX)
                .append('(').append(row.getIdValue()).append(')')
                .append(SQLStatements.FROM.getValue()).append(row.getTableName());
        return request.toString();
    }
}
