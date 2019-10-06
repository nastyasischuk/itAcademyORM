package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class AfterFromStepImpl implements AfterFromStep {
    private StringBuilder query;

    AfterFromStepImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public EndQuery orderBy(SimpleField column) {
        query.append(" order by ").append(column.getName());
        return new EndQueryImpl(query);
    }

    @Override
    public HavingStep groupBy(SimpleField column) {
        query.append(" group by ").append(column.getName());
        return new HavingStepImpl(query);
    }

    @Override
    public AfterFromStep where(String condition) {
        query.append(" where ").append(condition);
        return new AfterFromStepImpl(query);
    }

    @Override
    public AfterFromStep where(Condition condition) {
        query.append(" where ").append(condition.toString());
        return new AfterFromStepImpl(query);
    }

    @Override
    public AfterFromStep where() {
        query.append(" where ");
        return new AfterFromStepImpl(query);
    }

    @Override
    public String end() {
        return query.append(";").toString();
    }

    @Override
    public String toString() {
        return query.toString();
    }

}
