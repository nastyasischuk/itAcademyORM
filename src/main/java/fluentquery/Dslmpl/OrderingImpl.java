package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class OrderingImpl implements Ordering, EndQuery {
    private StringBuilder query;

    public OrderingImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public EndQuery asc() {
        query.append(" asc ");
        return new EndQueryImpl(query);
    }

    @Override
    public EndQuery desc() {
        query.append(" desc ");
        return new EndQueryImpl(query);
    }

    @Override
    public String end() {
        query.append(";");
        return query.toString();
    }
}
