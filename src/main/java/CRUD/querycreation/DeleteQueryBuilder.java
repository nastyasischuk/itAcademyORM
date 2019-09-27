package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class DeleteQueryBuilder extends QueryBuilder{

    public DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.DELETE.getValue()).append(SQLStatements.FROM.getValue())
                .append(row.getTableName()).append(SQLStatements.WHERE.getValue());
        row.getMap().forEach((k, v) -> request.append(k + "=" + v));
        request.append(';');
        return request.toString();
    }
}
