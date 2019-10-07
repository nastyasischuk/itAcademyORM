package customQuery;


import annotations.Table;
import exceptions.NoPrimaryKeyException;
import exceptions.SeveralPrimaryKeysException;

import annotations.AnnotationUtils;
import connection.DataBase;
import tablecreation.SQLStatements;

public class QueryBuilderImpl implements QueryBuilder {
    protected StringBuilder query;
    private Class<?> classType;
    private Limits limits;
    private Aggregation aggregation;
    private int lastIndexOfStar;
    private DataBase dataBase;

    public QueryBuilderImpl(Class<?> classType, DataBase dataBase) {
        limits = new Limits(classType);
        aggregation = new Aggregation(classType);
        this.dataBase = dataBase;
        query = new StringBuilder();
        this.classType = classType;
    }

    public QueryBuilderImpl(Class<?> classType) {
        limits = new Limits(classType);
        aggregation = new Aggregation(classType);
        query = new StringBuilder();
        this.classType = classType;
    }


    public Class<?> getClassType() {
        return classType;
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }


    @Override
    public String toString() {
        return query.toString();
    }



    public QueryBuilderImpl select() {
        query.append(SQLStatements.SELECT.getValue()).append(MarkingChars.star).append(SQLStatements.FROM.getValue())

                .append(AnnotationUtils.getTableName(classType));
        lastIndexOfStar = query.lastIndexOf("*");
        return this;
    }

    public QueryBuilderImpl select(Aggregation aggregation) {
        query.append(SQLStatements.SELECT.getValue()).append(aggregation.build()).append(SQLStatements.FROM.getValue())
                .append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl innerJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.INNER.getValue()).append(SQLStatements.JOIN.getValue()).append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl leftJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.LEFT.getValue()).append(SQLStatements.JOIN.getValue()).append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl rightJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.RIGHT.getValue()).append(SQLStatements.JOIN.getValue()).append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl fullOuterJoin(Class typeOfClass) {
        query.replace(lastIndexOfStar, lastIndexOfStar + 1, unionLaneForJoin(classType));
        query.append(SQLStatements.FULL.getValue()).append(SQLStatements.OUTER.getValue())
                .append(SQLStatements.JOIN.getValue()).append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl on(Limits limits) {
        query.append(SQLStatements.ON.getValue()).append(limits.build());
        return this;
    }

    public QueryBuilderImpl selectMath(Aggregation aggregation, Limits limits) {
        query.append(SQLStatements.SELECT.getValue()).append(aggregation.build()).append(limits.build()).append(SQLStatements.FROM.getValue())
                .append(AnnotationUtils.getTableName(classType));
        return this;
    }

    public QueryBuilderImpl where(Limits limits) {
        StringBuilder lane = new StringBuilder();
        query.append(lane.append(SQLStatements.WHERE.getValue()).append(limits.build()));
        return this;
    }


    public QueryBuilderImpl groupBy(Limits limits) {
        StringBuilder lane = new StringBuilder();
        setQuery(query.append(lane.append(SQLStatements.GROUP_BY.getValue()).append(limits.build())));
        return this;
    }


    public QueryBuilderImpl orderBy(Limits limits) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(limits.build());
        return this;
    }

    public QueryBuilderImpl orderBy() {
        query.append(SQLStatements.ORDER_BY.getValue());
        return this;
    }

    public QueryBuilderImpl orderBy(QueryBuilderImpl queryBuilderImpl, Limits limits) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(queryBuilderImpl).append(limits.build());
        return this;
    }

    public QueryBuilderImpl orderBy(Aggregation aggregation, QueryBuilderImpl queryBuilderImpl) {
        query.append(SQLStatements.ORDER_BY.getValue()).append(aggregation.build()).append(queryBuilderImpl);
        return this;
    }

    public QueryBuilderImpl having(Aggregation aggregation, Limits limits) {
        query.append(SQLStatements.HAVING.getValue()).append(aggregation.build()).append(MarkingChars.space).append(limits.build());
        return this;
    }

    public QueryBuilderImpl ascAndDesc(String ascColumnName, String descColumnName) {
        query.append(getLimits().getColumnName(ascColumnName)).append(SQLStatements.ASC.getValue()).append(MarkingChars.comma)
                .append(getLimits().getColumnName(descColumnName)).append(SQLStatements.DESC.getValue());
        return this;
    }

    public QueryBuilderImpl asc(String columnName) {
        query.append(getLimits().getColumnName(columnName)).append(SQLStatements.ASC.getValue());
        return this;
    }

    public QueryBuilderImpl asc() {
        query.append(SQLStatements.ASC.getValue());
        return this;
    }

    public QueryBuilderImpl desc(String columnName) {
        query.append(getLimits().getColumnName(columnName)).append(SQLStatements.DESC.getValue());
        return this;
    }

    public QueryBuilderImpl desc() {
        query.append(SQLStatements.DESC.getValue());
        return this;
    }



    public Limits getLimits() {
        return limits;
    }


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
            values.append(AnnotationUtils.getTableName(classType)).append(MarkingChars.dot).append(columnName);
            if (!columnName.equals(lastElement)) {
                values.append(MarkingChars.comma);
            } else {
                break;
            }
        }
        return values.toString();
    }

    public String getQuery() {
        return query.toString();
    }

    @Override
    public DataBase getDataBase() {
        return this.dataBase;
    }}