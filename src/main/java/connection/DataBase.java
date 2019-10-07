package connection;

import CRUD.CRUD;
import customQuery.QueryBuilder;
import customQuery.QueryBuilderImpl;
import fluentquery.Dslmpl.QueryOrderedImpl;
import transaction.TransactionsManager;

import java.sql.Connection;
import java.sql.Statement;

public interface DataBase {
    void openConnection();

    Connection getConnection();

    void close();

    Statement getStatement(String s);

    CRUD getCrud();

    void closeStatement(Statement statement);
    TransactionsManager getTransactionManager();
    QueryBuilderImpl getQueryBuilder(Class<?> classType);
    QueryOrderedImpl getQueryOrdered(Class<?> classType);

}
