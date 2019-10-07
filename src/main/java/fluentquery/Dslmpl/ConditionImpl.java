package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class ConditionImpl implements Condition {
    private StringBuilder connectCondition;

    public ConditionImpl(StringBuilder queryCondition) {
        connectCondition = queryCondition;
    }

    @Override
    public Condition and(Condition condition) {
        connectCondition.append(" and ").append(condition);
        return new ConditionImpl(connectCondition);
    }

    @Override
    public Condition or(Condition condition) {
        connectCondition.append(" or ").append(condition);
        return new ConditionImpl(connectCondition);
    }

    @Override
    public Condition andNot(Condition condition) {
        connectCondition.append(" and not ").append(condition);
        return new ConditionImpl(connectCondition);
    }

    @Override
    public Condition orNot(Condition condition) {
        connectCondition.append(" or not ").append(condition);
        return new ConditionImpl(connectCondition);
    }

    @Override
    public Condition not() {
        connectCondition.append(" not ");
        return new ConditionImpl(connectCondition);
    }

    @Override
    public String toString() {
        return connectCondition.toString();
    }
}
