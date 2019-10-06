package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class StartClass {

    public static From select() {
        StringBuilder query = new StringBuilder("select * ");
        return new FromImpl(query);
    }

    public static From select(SimpleField... fields) {
        StringBuilder query = new StringBuilder("select ");
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
}
