package customQuery;

import tablecreation.SQLStatements;


public class SubQuery extends QueryImpl {

    public SubQuery(Class<?> classType) {
        super(classType);
    }

    public String getQuery() {
        return query.toString();
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    public SubQuery select(String columnName) {
        query.append(SQLStatements.SELECT.getValue()).append(getLimits().getColumnName(columnName)).append(SQLStatements.FROM.getValue())
                .append(getFromClassTableName(getClassType()));
        return this;
    }

    public SubQuery builder() {
        return new SubQuery(getClassType());
    }
}
