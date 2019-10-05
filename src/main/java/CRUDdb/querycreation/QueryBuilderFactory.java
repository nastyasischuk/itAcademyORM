package CRUDdb.querycreation;


import CRUDdb.rowhandler.RowFromDB;
import CRUDdb.rowhandler.RowToDB;

public class QueryBuilderFactory {
    public QueryBuilder createQueryBuilder(RowToDB row, QueryType queryType){
        switch (queryType){
            case INSERT:
                return new InsertQueryBuilder(row);
            case DELETE:
                return new DeleteQueryBuilder(row);
            case UPDATE:
                return new UpdateQueryBuilder(row);
            case SELECTID:
                return new SelectLatestIdQueryBuilder(row);
        }
        return null;
    }
    public QueryBuilderFromDB createQueryBuilderFromDB(RowFromDB row,QueryType queryType){
        if(queryType==QueryType.SELECT_OBJECT)
        return new QueryBuilderFromDB(row);
        if(queryType==QueryType.SELECT_MANYTOMANY)
            return new QueryBuilderFromDBManyToMany(row);
        return null;
    }


}
