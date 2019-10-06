package customQuery;

import tablecreation.SQLStatements;


public class QueryImpl implements QueryBuilder {
    protected StringBuilder query;
    private Class<?> classType;
    private Limits limits;
    private Aggregation aggregation;
    private int lastIndexOfStar;

    public QueryImpl(Class<?> classType) {
        limits = new Limits(classType);
        aggregation = new Aggregation(classType);
        query = new StringBuilder();
        this.classType = classType;
    }


    public StringBuilder getQuery() {
        return query;
    }

    Class<?> getClassType() {
        return classType;
    }

    protected void setQuery(StringBuilder query) {
        this.query = query;
    }


    @Override
    public String toString() {
        return query.toString();
    }


    @Override
    public QueryImpl select() {
        query.append(SQLStatements.SELECT.getValue()).append(MarkingChars.star).append(SQLStatements.FROM.getValue())
                .append(classType.getSimpleName());
        lastIndexOfStar = query.lastIndexOf("*");
        return this;
    }

    public QueryImpl select(Aggregation aggregation) {
        query.append(SQLStatements.SELECT.getValue()).append(aggregation.build()).append(SQLStatements.FROM.getValue())
                .append(classType.getSimpleName());
        return this;
    }

    public QueryImpl innerJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.INNER.getValue()).append(SQLStatements.JOIN.getValue()).append(typeOfClass.getSimpleName());
        return this;
    }

    public QueryImpl leftJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.LEFT.getValue()).append(SQLStatements.JOIN.getValue()).append(typeOfClass.getSimpleName());
        return this;
    }

    public QueryImpl rightJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.RIGHT.getValue()).append(SQLStatements.JOIN.getValue()).append(typeOfClass.getSimpleName());
        return this;
    }

    public QueryImpl fullOuterJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.FULL.getValue()).append(SQLStatements.OUTER.getValue())
                .append(SQLStatements.JOIN.getValue()).append(typeOfClass.getSimpleName());
        return this;
    }

    public QueryImpl on(Limits limits) {
        query.append(SQLStatements.ON.getValue()).append(limits.build());
        return this;
    }

    public QueryImpl selectMath(Aggregation aggregation, Limits limits) {
        query.append(SQLStatements.SELECT.getValue()).append(aggregation.build()).append(limits.build()).append(SQLStatements.FROM.getValue())
                .append(classType.getSimpleName());
        return this;
    }

    @Override
    public QueryImpl where(Limits limits) {
        StringBuilder lane = new StringBuilder();
        query.append(lane.append(SQLStatements.WHERE.getValue()).append(limits.build()));
        return this;
    }

    @Override
    public QueryImpl groupBy(Limits limits) {
        StringBuilder lane = new StringBuilder();
        setQuery(query.append(lane.append(SQLStatements.GROUP_BY.getValue()).append(limits.build())));
        return this;
    }

    @Override
    public QueryImpl orderBy(Limits limits) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(limits.build());
        return this;
    }

    public QueryImpl orderBy(QueryImpl queryImpl) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(queryImpl);
        return this;
    }

    public QueryImpl orderBy(QueryImpl queryImpl, Limits limits) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(queryImpl).append(limits.build());
        return this;
    }

    public QueryImpl orderBy(Aggregation aggregation, QueryImpl queryImpl) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(aggregation.build()).append(queryImpl);
        return this;
    }

    public QueryImpl having(Aggregation aggregation, Limits limits) {
        query.append(SQLStatements.HAVING.getValue()).append(aggregation.build()).append(MarkingChars.space).append(limits.build());
        return this;
    }

    public QueryImpl ascAndDesc(String ascColumnName, String descColumnName) {

        query.append(getLimits().getColumnName(ascColumnName)).append(SQLStatements.ASC.getValue()).append(MarkingChars.comma)
                .append(getLimits().getColumnName(descColumnName)).append(SQLStatements.DESC.getValue());
        return this;
    }

    public QueryImpl asc(String columnName) {
        query.append(getLimits().getColumnName(columnName)).append(SQLStatements.ASC.getValue());
        return this;
    }

    public QueryImpl asc() {
        query.append(SQLStatements.ASC.getValue());
        return this;
    }

    public QueryImpl desc(String columnName) {
        query.append(getLimits().getColumnName(columnName)).append(SQLStatements.DESC.getValue());
        return this;
    }

    public QueryImpl desc() {
        query.append(SQLStatements.DESC.getValue());
        return this;
    }


    @Override
    public Limits getLimits() {
        return limits;
    }

    @Override
    public Aggregation getAggregates() {
        return aggregation;
    }

    public String fetch() {
        query.append(MarkingChars.semicolon);
        return query.toString();
    }

    private String unionLaneForJoin(Class classType) {
        StringBuilder values = new StringBuilder();
        for (String columnName : getLimits().getAllColumnNames(classType)) {
            String lastElement = getLimits().getAllColumnNames(classType).get(getLimits().getAllColumnNames(classType).size() - 1);
            values.append(classType.getSimpleName()).append(MarkingChars.dot).append(columnName);
            if (!columnName.equals(lastElement)) {
                values.append(MarkingChars.comma);
            } else {
                break;
            }
        }
        return values.toString();
    }

    public QueryImpl buildQuery() {
        return new QueryImpl(classType);
    }
}
