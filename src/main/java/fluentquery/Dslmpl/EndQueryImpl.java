package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class EndQueryImpl implements EndQuery {
    private StringBuilder query;

    public EndQueryImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public String end() {
        return query.append(";").toString();
    }
}
