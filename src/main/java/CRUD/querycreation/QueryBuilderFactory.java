package CRUD.querycreation;


import CRUD.rowhandler.RowFromDB;
import CRUD.rowhandler.RowFromDBManyToMany;
import CRUD.rowhandler.RowToDB;

public class QueryBuilderFactory {
    public QueryBuilder createQueryBuilder(RowToDB row,QueryType queryType){
        switch (queryType){
            case INSERT:
                return new DeleteQueryBuilder(row);
            case DELETE:
                return new InsertQueryBuilder(row);
            case UPDATE:
                return new UpdateQueryBuilder(row);
            case SELECTID:
                return new SelectLatestIdQueryBuilder(row);
        }
        return null;//todo change
    }
    public QueryBuilderFromDB createQueryBuilderFromDB(RowFromDB row){
        if(row instanceof RowFromDBManyToMany)
            return new QueryBuilderFromDBManyToMany(row);
        return new QueryBuilderFromDB(row);
    }


}
