package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class SelectLatestIdQueryBuilder extends QueryBuilder {
    public SelectLatestIdQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(SQLStatements.MAX)
                .append('(').append(row.getIdName()).append(')')
                .append(SQLStatements.FROM.getValue()).append(row.getTableName()).append(";");
        return query.toString();
    }
}
