package customQuery;

import connection.DataBase;

public interface QueryBuilder {

    QueryImpl select();

    QueryImpl where(Limits limits);

    QueryImpl groupBy(Limits limits);

    QueryImpl orderBy(Limits limits);

    Limits getLimits();

    Aggregation getAggregates();
    String getQuery();
    DataBase getDataBase();
    Class<?> getClassType();

}
