package CRUD.requests;

import tablecreation.SQLStatements;

public class CustomQuery {
    private StringBuilder str = new StringBuilder();

    public CustomQuery() {
    }

    public CustomQuery(Object o) {

    }

    public void setStr(StringBuilder str) {
        this.str = str;
    }

    public static QueryTestBuilder buildQuery() {
        return new CustomQuery().new QueryTestBuilder();
    }

    @Override
    public String toString() {
        return str.toString();
    }

    public class QueryTestBuilder {
        CustomQuery customQuery;

        private QueryTestBuilder() {
            customQuery = new CustomQuery();
        }

        public QueryTestBuilder select(String... columnNames) {
            customQuery.setStr(str.append(SQLStatements.SELECT.getValue()));
            if (columnNames.length == 0) {
                customQuery.setStr(str.append(MarkingChars.star));
            } else {
                customQuery.setStr(str.append(commaSeparation(columnNames)));
            }
            return this;
        }

        public QueryTestBuilder from(String tableName) {
            customQuery.setStr(str.append(SQLStatements.FROM.getValue()).append(tableName));
            return this;
        }

        public QueryTestBuilder where() {
            customQuery.setStr(str.append(SQLStatements.WHERE.getValue()));
            return this;
        }

        public QueryTestBuilder where(Object o) {
            //todo Where
            return this;
        }

        public QueryTestBuilder in(String... values) {
            customQuery.setStr(str.append(SQLStatements.IN.getValue()).append(MarkingChars.openBracket));
            for (String value : values) {
                String lastElement = values[values.length - 1];
                customQuery.setStr(str.append(MarkingChars.quote).append(value)
                        .append(MarkingChars.quote));
                if (!value.equals(lastElement)) {
                    customQuery.setStr(str.append(MarkingChars.comma));
                } else {
                    break;
                }
            }
            customQuery.setStr(str.append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder in(CustomQuery customQuery) {
            customQuery.setStr(str.append(SQLStatements.IN).append(MarkingChars.openBracket)
                    .append(customQuery).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder between(String valueFrom, String valueTill) {
            customQuery.setStr(str.append(SQLStatements.BETWEEN.getValue()).append(MarkingChars.quote)
                    .append(valueFrom).append(MarkingChars.quote).append(SQLStatements.AND.getValue())
                    .append(MarkingChars.quote).append(valueTill).append(MarkingChars.quote));
            return this;
        }

        public QueryTestBuilder orderBy(String... columnNames) {
            customQuery.setStr(str.append(SQLStatements.ORDER_BY.getValue()).append(commaSeparation(columnNames)));
            return this;
        }

        public QueryTestBuilder having(String columnName, String condition) {
            customQuery.setStr(str.append(SQLStatements.HAVING.getValue()).append(SQLStatements.COUNT.getValue())
                    .append(MarkingChars.openBracket).append(columnName).append(MarkingChars.closedBracket)
                    .append(MarkingChars.space).append(condition));
            return this;
        }

        public QueryTestBuilder exists(CustomQuery newCustomQuery) {
            customQuery.setStr(str.append(SQLStatements.EXISTS.getValue()).append(MarkingChars.openBracket)
                    .append(newCustomQuery).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder count(String columnName) {
            customQuery.setStr(str.append(SQLStatements.COUNT.getValue()).append(MarkingChars.openBracket)
                    .append(columnName).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder groupBy(String columnName) {
            customQuery.setStr(str.append(SQLStatements.GROUP_BY.getValue()).append(columnName));
            return this;
        }

        public QueryTestBuilder selectMin(String columnName) {
            customQuery.setStr(str.append(SQLStatements.MIN.getValue()).append(MarkingChars.openBracket)
                    .append(columnName).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder selectMax(String columnName) {
            customQuery.setStr(str.append(SQLStatements.MAX.getValue()).append(MarkingChars.openBracket)
                    .append(columnName).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder AVG(String columnName) {
            customQuery.setStr(str.append(SQLStatements.AVG.getValue()).append(MarkingChars.openBracket)
                    .append(columnName).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder SUM(String columnName) {
            customQuery.setStr(str.append(SQLStatements.SUM.getValue()).append(MarkingChars.openBracket)
                    .append(columnName).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder and() {
            customQuery.setStr(str.append(SQLStatements.AND.getValue()));
            return this;
        }

        public QueryTestBuilder andOr(String value, String SecondValue) {//TODO Wait before where will be ready, and then
            customQuery.setStr(str.append(SQLStatements.AND.getValue()).append(MarkingChars.openBracket)
                    .append(value).append(SQLStatements.OR.getValue()).append(SecondValue).append(MarkingChars.closedBracket));
            return this;
        }

        public QueryTestBuilder not() {
            customQuery.setStr(str.append(SQLStatements.NOT.getValue()));
            return this;
        }

        public QueryTestBuilder andNot(String value) {//TODO Wait before where will be ready, and then
            customQuery.setStr(str.append(SQLStatements.AND.getValue()).append(SQLStatements.NOT.getValue())
                    .append(value));
            return this;
        }

        public QueryTestBuilder or() {
            customQuery.setStr(str.append(SQLStatements.OR.getValue()));
            return this;
        }

        public QueryTestBuilder is() {
            customQuery.setStr(str.append(SQLStatements.IS.getValue()));
            return this;
        }

        public QueryTestBuilder nuLL() {
            customQuery.setStr(str.append(SQLStatements.NULL.getValue()));
            return this;
        }

        public QueryTestBuilder asc() {
            customQuery.setStr(str.append(SQLStatements.ASC.getValue()));
            return this;
        }

        public QueryTestBuilder desc() {
            customQuery.setStr(str.append(SQLStatements.DESC.getValue()));
            return this;
        }

        public QueryTestBuilder close() {
            customQuery.setStr(str.append(MarkingChars.semicolon));
            return this;
        }

        private StringBuilder commaSeparation(String[] columnNames) {
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
            return values;
        }

        public CustomQuery build() {
            return CustomQuery.this;
        }
    }
}
