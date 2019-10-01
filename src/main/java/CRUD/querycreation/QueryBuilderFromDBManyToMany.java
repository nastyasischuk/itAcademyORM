package CRUD.querycreation;

import CRUD.rowhandler.RowFromDB;

public class QueryBuilderFromDBManyToMany {
    RowFromDB rowFromDB;
    String nameJoinTable;

    public QueryBuilderFromDBManyToMany(RowFromDB rowFromDB,String nameJoinTable) {
        this.rowFromDB = rowFromDB;
        this.nameJoinTable = nameJoinTable;
    }
    public String buildQuery(){
        return null;//example of query to build
        // select * from tablename from row
       // join nameJoinTable p on cat.id = p.c_id
        //where nameJoinTable.id_name =id_value;
    }
}