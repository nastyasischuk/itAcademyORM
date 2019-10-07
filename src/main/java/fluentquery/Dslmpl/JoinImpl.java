package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

class JoinImpl implements JoinStep {

    private StringBuilder query;

    JoinImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public AfterFromStep on(Condition condition) {
        query.append(" on ").append(condition);
        return new AfterFromStepImpl(query);
    }

    @Override
    public AfterFromStep using(SimpleField... fields) {
        query.append(" using ").append(" (");
        for (SimpleField field : fields) {
            query.append(field.getName()).append(", ");
        }
        query.append(")");
        String queryStr = query.toString();
        if (queryStr.endsWith(", )"))
            queryStr = queryStr.replace(", )", ")");

        query = new StringBuilder(queryStr);
        return new AfterFromStepImpl(query);
    }

    @Override
    public AfterFromStep as(SimpleField field) {
        query.append(" as ").append(field.getName());
        return new AfterFromStepImpl(query);
    }
}
