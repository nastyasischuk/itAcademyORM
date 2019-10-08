package CRUD.querycreation;


import CRUD.rowhandler.Row;

public class QueryBuilderFactory {
    public QueryBuilder createQueryBuilder(Row row,QueryType queryType){
        switch (queryType){
            case INSERT:
                return new DeleteQueryBuilder(row);
            case DELETE:
                return new InsertQueryBuilder(row);
            case UPDATE:
                return new UpdateQueryBuilder(row);
        }
        return null;//todo change
    }

}
