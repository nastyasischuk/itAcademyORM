package CRUD.customQuery;

import tablecreation.SQLStatements;


public class SubQuery extends QueryImpl {
    private StringBuilder query = new StringBuilder();


    public SubQuery(Class<?> classType) {
        super(classType);
    }

    public StringBuilder getQuery() {
        return query;
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    public SubQuery selectWhere(String columnName) {
        query.append(SQLStatements.SELECT.getValue()).append(getLimits().createColumn(columnName)).append(SQLStatements.FROM.getValue())
                .append(getClassType().getSimpleName()).append(SQLStatements.WHERE.getValue());
        return this;
    }

    public SubQuery ascAndDesc(String ascColumnName, String descColumnName) {

        query.append(getLimits().createColumn(ascColumnName)).append(SQLStatements.ASC.getValue()).append(MarkingChars.comma)
                .append(getLimits().createColumn(descColumnName)).append(SQLStatements.DESC.getValue());
        return this;
    }

    public SubQuery asc(String columnName) {
        query.append(getLimits().createColumn(columnName)).append(SQLStatements.ASC.getValue());
        return this;
    }

    public SubQuery asc() {
        query.append(SQLStatements.ASC.getValue());
        return this;
    }

    public SubQuery desc(String columnName) {
        query.append(getLimits().createColumn(columnName)).append(SQLStatements.DESC.getValue());
        return this;
    }
    public SubQuery desc() {
        query.append(SQLStatements.DESC.getValue());
        return this;
    }

    public SubQuery equals(String... columnNames) {
        StringBuilder lane = new StringBuilder();
        setQuery(query.append(lane.append(commaSeparation(columnNames))));
        return this;
    }

    private String commaSeparation(String[] columnNames) {
        StringBuilder values = new StringBuilder();
        for (String columnName : columnNames) {
            String lastElement = columnNames[columnNames.length - 1];
            values.append(columnName);
            if (!columnName.equals(lastElement)) {
                values.append(MarkingChars.comma);
            } else {
                break;
            }
        }
        return values.toString();
    }

    public String build() {
        return query.toString();
    }
    public SubQuery builder() {
        return new SubQuery(getClassType());
    }
}
