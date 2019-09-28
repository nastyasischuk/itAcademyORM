package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

import java.util.Map;

public class DeleteQueryBuilder extends QueryBuilder{
    public DeleteQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.DELETE.getValue()).append(SQLStatements.FROM.getValue()).append(row.getTableName()).append(SQLStatements.WHERE.getValue());
        for(Map.Entry<String,String> entry: row.getMap().entrySet()){
           //todo add
        }
        request.append(';');
        return request.toString();
    }
}
