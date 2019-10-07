package customQuery;

import tablecreation.SQLStatements;


public class SubQueryBuilder extends QueryBuilderImpl {

    public SubQueryBuilder(Class<?> classType) {
        super(classType);
    }

    public String getQuery() {
        return query.toString();
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    public SubQueryBuilder select(String columnName) {
        query.append(SQLStatements.SELECT.getValue()).append(getLimits().getColumnName(columnName)).append(SQLStatements.FROM.getValue())
                .append(getClassType().getSimpleName());
        return this;
    }

    public SubQueryBuilder builder() {
        return new SubQueryBuilder(getClassType());
    }
}
