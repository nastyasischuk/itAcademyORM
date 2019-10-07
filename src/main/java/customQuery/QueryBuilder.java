package customQuery;

import connection.DataBase;

public interface QueryBuilder {

    QueryBuilderImpl select();

    QueryBuilderImpl where(Limits limits);

    QueryBuilderImpl groupBy(Limits limits);

    QueryBuilderImpl orderBy(Limits limits);

    Limits getLimits();

    Aggregation getAggregates();
    String getQuery();
    DataBase getDataBase();
    Class<?> getClassType();

}
