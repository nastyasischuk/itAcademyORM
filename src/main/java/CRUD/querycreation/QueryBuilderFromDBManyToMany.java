package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowFromDB;
import CRUD.rowhandler.RowFromDBManyToMany;
import tablecreation.SQLStatements;

public class QueryBuilderFromDBManyToMany extends QueryBuilderFromDB{
    private RowFromDBManyToMany row;


    public QueryBuilderFromDBManyToMany(RowFromDB rowFromDB) {
        super(rowFromDB);
        this.row = (RowFromDBManyToMany) rowFromDB;

    }
    public String buildQuery(){
        StringBuilder query = new StringBuilder();
      query.append(SQLStatements.SELECT.getValue()).append(row.getInverse_column())
              .append(SQLStatements.FROM.getValue()).append(row.getManyToManyTableName()).
              append(SQLStatements.WHERE.getValue())
              .append(row.getJoin_coulmn()).append(MarkingChars.equally).append(row.getIdValue()).append(MarkingChars.semicolon);
        return query.toString();

    }
}