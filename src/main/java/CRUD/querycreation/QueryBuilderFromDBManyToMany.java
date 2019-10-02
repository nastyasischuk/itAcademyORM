package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowFromDB;
import tablecreation.SQLStatements;

public class QueryBuilderFromDBManyToMany {
    RowFromDB rowFromDB;
    String nameJoinTable;

    public QueryBuilderFromDBManyToMany(RowFromDB rowFromDB,String nameJoinTable) {
        this.rowFromDB = rowFromDB;
        this.nameJoinTable = nameJoinTable;
    }
    public String buildQuery(){
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.SELECT.getValue()).append(MarkingChars.star).append(SQLStatements.FROM.getValue())
                .append(rowFromDB.getTableName()).append(SQLStatements.JOIN.getValue()).append(nameJoinTable)
                .append(SQLStatements.ON.getValue()).append(rowFromDB.getTableName()).append(MarkingChars.dot).append(rowFromDB.getIdName())
                .append(MarkingChars.equally).append(nameJoinTable).append(MarkingChars.dot).append(rowFromDB.getIdName())
                .append(SQLStatements.INNER.getValue()).append(SQLStatements.JOIN.getValue()).append(SQLStatements.WHERE.getValue())
                .append(nameJoinTable).append(MarkingChars.dot).append(rowFromDB.getIdName()).append(MarkingChars.equally)
                .append(rowFromDB.getIdValue()).append(MarkingChars.semicolon);
        return query.toString();//example of query to build
        // select * from tablename from row
       // join nameJoinTable p on cat.id = p.c_id
        //where nameJoinTable.id_name =id_value;
    }
}