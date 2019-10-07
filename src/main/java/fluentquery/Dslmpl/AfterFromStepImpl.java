package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class AfterFromStepImpl implements AfterFromStep {
    private StringBuilder query;

    AfterFromStepImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public Ordering orderBy(SimpleField column) {
        query.append(" order by ").append(column.getName());
        return new OrderingImpl(query);
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
        query.append(" where ").append(condition.toString()); //TODO toString
        return new AfterFromStepImpl(query);
    }

    @Override
    public AfterFromStep where() {
        query.append(" where ");
        return new AfterFromStepImpl(query);
    }

    public JoinStep join(Table table) {
        query.append(" join ").append(table.getName());
        return new JoinImpl(query);
    }

    public JoinStep leftJoin(Table table) {
        query.append(" left join ").append(table.getName());
        return new JoinImpl(query);
    }

    public JoinStep innerJoin(Table table) {
        query.append(" inner join ").append(table.getName());
        return new JoinImpl(query);
    }

    public JoinStep rightJoin(Table table) {
        query.append(" right join ").append(table.getName());
        return new JoinImpl(query);
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
