package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;


public class HavingStepImpl implements HavingStep, Ordering {
    private StringBuilder query;

    HavingStepImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public EndQuery having(Condition condition) {
        query.append("  having ").append(condition.toString());
        return new EndQueryImpl(query);
    }

    @Override
    public String end() {
        query.append(";");
        return query.toString();
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
}
