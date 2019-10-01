package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowFromDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDB {
    RowFromDB rowFromDB;

    public QueryBuilderFromDB(RowFromDB rowFromDB) {
        this.rowFromDB = rowFromDB;
    }
    public String buildQuery(){
        //todo for method findCollection
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.SELECT.getValue()).append(MarkingChars.star).append(rowFromDB.getTableName());
        return request.toString();

    }
}
