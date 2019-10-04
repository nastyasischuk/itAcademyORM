package CRUD.customQuery;

import tablecreation.SQLStatements;


public class QueryImpl implements Query {
    private StringBuilder query;
    private Class<?> classType;
    private Limits limits;
    private Aggregation aggregation;

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
        StringBuilder lane = new StringBuilder();
        setQuery(query.append(lane.append(SQLStatements.SELECT.getValue()).append(MarkingChars.star).append(SQLStatements.FROM.getValue())
                .append(classType.getSimpleName())));
        return this;
    }

    public QueryImpl select(Aggregation aggregation) {
        query.append(SQLStatements.SELECT.getValue()).append(aggregation.build()).append(SQLStatements.FROM.getValue())
                .append(classType.getSimpleName());
        return this;
    }

    public QueryImpl selectMath(Aggregation aggregation, Limits limits){
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

    public QueryImpl orderBy(SubQuery subQuery) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(subQuery.build());
        return this;
    }

    public QueryImpl orderBy(SubQuery subQuery, Limits limits) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(subQuery.build()).append(limits.build());
        return this;
    }

    public QueryImpl orderBy(Aggregation aggregation, SubQuery subQuery) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(aggregation.build()).append(subQuery.build());
        return this;
    }

    public QueryImpl having(Aggregation aggregation,Limits limits) {
        query.append(SQLStatements.HAVING.getValue()).append(aggregation.build()).append(MarkingChars.space).append(limits.build());
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

    public QueryImpl fetch() {
        query.append(MarkingChars.semicolon);
        return this;
    }

    public QueryImpl buildQuery(){
        return new QueryImpl(classType);
    }
}
