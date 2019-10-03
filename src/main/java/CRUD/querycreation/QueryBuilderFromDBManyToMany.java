package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowFromDB;
import CRUD.rowhandler.RowFromDBManyToMany;
import CRUD.rowhandler.RowToDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDBManyToMany {
    RowFromDBManyToMany row;
    RowFromDB rowFromDB;
    String nameJoinTable;

    public QueryBuilderFromDBManyToMany(RowFromDB rowFromDB,String nameJoinTable) {
        this.rowFromDB = rowFromDB;
        this.nameJoinTable = nameJoinTable;
    }
    public String buildQuery(){
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(rowFromDB.getTableName()).append(MarkingChars.dot).append(rowFromDB.getIdName())
                .append(MarkingChars.comma).append(row.getTableName()).append(row.getInverse_column()).append(row.getJoin_coulmn())
                .append(SQLStatements.FROM.getValue()).append(rowFromDB.getTableName()).append(SQLStatements.INNER).append(SQLStatements.JOIN.getValue())
                .append(row.getManyToManyTableName()).append(SQLStatements.ON.getValue()).append(rowFromDB.getTableName()).append(MarkingChars.dot)
                .append(rowFromDB.getIdName()).append(MarkingChars.equally).append(row.getManyToManyTableName()).append(MarkingChars.dot)
                .append(row.getInverse_column()).append(SQLStatements.WHERE.getValue()).append(row.getManyToManyTableName()).append(MarkingChars.dot)
                .append(row.getJoin_coulmn()).append(MarkingChars.equally).append(rowFromDB.getIdValue()).append(MarkingChars.semicolon);
        return query.toString();//example of query to build
        // select * from tablename from row
       // join nameJoinTable p on cat.id = p.c_id
        //where nameJoinTable.id_name =id_value;
    }
}