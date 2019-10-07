package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class FromImpl implements From {
    private StringBuilder query;

    FromImpl(StringBuilder stringBuilder) {
        this.query = stringBuilder;
    }

    @Override
    public AfterFromStep from(Table table) {
        query.append(" from ").append(table.getName());
        return new AfterFromStepImpl(query);
    }
}
