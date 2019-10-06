package CRUDdb.querycreation;

import CRUDdb.requests.MarkingChars;
import CRUDdb.rowhandler.RowFromDB;
import CRUDdb.rowhandler.RowFromDBManyToMany;
import tablecreation.SQLStatements;

public class QueryBuilderFromDBManyToMany extends QueryBuilderFromDB{
    private RowFromDBManyToMany row;


    public QueryBuilderFromDBManyToMany(RowFromDB rowFromDB) {
        super(rowFromDB);
        this.row = (RowFromDBManyToMany) rowFromDB;

    }
    public String buildQuery(){
        StringBuilder query = new StringBuilder();
      query.append(SQLStatements.SELECT.getValue()).append(row.getInverseColumn())
              .append(SQLStatements.FROM.getValue()).append(row.getManyToManyTableName()).
              append(SQLStatements.WHERE.getValue())
              .append(row.getJoinCoulmn()).append(MarkingChars.equally).append(row.getIdValue()).append(MarkingChars.semicolon);
        return query.toString();

    }
}