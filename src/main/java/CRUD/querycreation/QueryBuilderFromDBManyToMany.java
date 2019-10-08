package CRUD.querycreation;

import CRUD.rowhandler.RowFromDB;
import CRUD.rowhandler.RowFromDBManyToMany;
import customQuery.MarkingChars;
import tablecreation.SQLStatements;

public class QueryBuilderFromDBManyToMany extends QueryBuilderFromDB {
    private RowFromDBManyToMany row;

    QueryBuilderFromDBManyToMany(RowFromDB rowFromDB) {
        super(rowFromDB);
        this.row = (RowFromDBManyToMany) rowFromDB;

    }

    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(row.getInverseColumn())
                .append(SQLStatements.FROM.getValue()).append(row.getManyToManyTableName())
                .append(SQLStatements.WHERE.getValue()).append(row.getJoinCoulmn()).append(MarkingChars.EQUALLY)
                .append(row.getIdValue()).append(MarkingChars.SEMICOLON);
        return query.toString();
    }
}