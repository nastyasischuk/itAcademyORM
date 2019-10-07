package fluentquery.Dslmpl;

import connection.DataBase;
import customQuery.QueryBuilder;
import fluentquery.Dslnterfaces.Field;
import fluentquery.Dslnterfaces.From;
import fluentquery.Dslnterfaces.SimpleField;
import fluentquery.Dslnterfaces.SkippableField;
import fluentquery.Dslnterfaces.*;

public class QueryOrderedImpl implements QueryBuilder {
    private DataBase dataBase;
    private Class classToReturn;
    private StringBuilder query;

    public QueryOrderedImpl(DataBase dataBase, Class classToReturn) {
        this.dataBase = dataBase;
        this.classToReturn = classToReturn;
    }

    public From select() {
        query = new StringBuilder("select * ");
        return new FromImpl(query);
    }

    public From select(SimpleField... fields) {
        query = new StringBuilder("select ");
        for (SimpleField column : fields)
            query.append(column.getName()).append(", ");

        String queryStr = query.toString();
        if (queryStr.endsWith(", "))
            queryStr = queryStr.substring(0, queryStr.length() - 2);

        query = new StringBuilder(queryStr);

        return new FromImpl(query);
    }

    public static AggregateImpl avg(Field field) {
        return new AggregateImpl(" avg(" + field.getName() + ") ");
    }

    public static AggregateImpl max(Field field) {
        return new AggregateImpl(" max(" + field.getName() + ") ");
    }

    public static AggregateImpl min(Field field) {
        return new AggregateImpl(" min(" + field.getName() + ") ");
    }

    public static AggregateImpl count(Field field) {
        return new AggregateImpl(" count(" + field.getName() + ") ");
    }

    public static AggregateImpl count() {
        return new AggregateImpl(" count(*) ");
    }

    public static AggregateImpl sum(Field field) {
        return new AggregateImpl(" sum(" + field.getName() + ") ");
    }

    public static FieldImpl column(String column) {
        return new FieldImpl(column);
    }

    public static SkippableField skip() {
        return new SkippableFieldImpl();
    }

    public static SimpleFieldImpl simpleField(String column) {
        return new SimpleFieldImpl(column);
    }

    public static TableImpl table(String tableName) {
        return new TableImpl(tableName);
    }

    @Override
    public String getQuery() {
        return query.toString();
    }

    @Override
    public DataBase getDataBase() {
        return dataBase;
    }

    @Override
    public Class<?> getClassType() {
        return classToReturn;
    }

}
