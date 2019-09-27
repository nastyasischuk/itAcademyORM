package CRUD.querycreation;


import CRUD.rowhandler.Row;
import tablecreation.SQLStatements;

public class QueryBuilderFactory {
    public QueryBuilder createQueryBuilder(Row row,SQLStatements sqlStatements){
        switch (sqlStatements){
            case INSERT:
                return new InsertQueryBuilder(row);
            case DELETE:
                return new DeleteQueryBuilder(row);
            case UPDATE:
                return new UpdateQueryBuilder(row);
        }
        return null;//todo change
    }

}
