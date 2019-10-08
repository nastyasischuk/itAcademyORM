package customQuery;

import connection.DataBase;

public interface QueryBuilder {
    String getQuery();
    DataBase getDataBase();
    Class<?> getClassType();
}
