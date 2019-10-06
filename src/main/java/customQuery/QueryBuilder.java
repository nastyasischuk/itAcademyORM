package customQuery;

public interface QueryBuilder {

    public QueryImpl select();

    public QueryImpl  where(Limits limits);

    public QueryImpl groupBy(Limits limits);

    public QueryImpl orderBy(Limits limits);

    public Limits getLimits();

    public Aggregation getAggregates();

}
