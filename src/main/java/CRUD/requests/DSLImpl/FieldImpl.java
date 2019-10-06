package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class FieldImpl extends SimpleFieldImpl implements Field {
    private StringBuilder queryCondition;

    FieldImpl(String name) {
        super(name);
        queryCondition = new StringBuilder();
        queryCondition.append(name);
    }

    @Override
    public Condition isNull() {
        queryCondition.append(" is null ");
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition isNotNull() {
        queryCondition.append(" is not null ");
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition like(String like) {
        queryCondition.append(" like ").append(like);
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition notLike(String notLike) {
        queryCondition.append(" not like ").append(notLike);
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition in(AfterFromStep selectFrom) {
        queryCondition.append(" in ").append(" ( ").append(selectFrom.toString()).append(" ) ");
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition notIn(AfterFromStep selectFrom) {
        queryCondition.append(" not in ").append(" (").append(selectFrom.toString()).append(") ");
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition between(Object first, Object last) {
        queryCondition.append(" between ").append(first.toString()).append(" and ").append(last.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition equal(Object value) {
        queryCondition.append(" = ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition notEqual(Object value) {
        queryCondition.append(" <> ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition less(Object value) {
        queryCondition.append(" < ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition lessOrEqual(Object value) {
        queryCondition.append(" <= ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition greater(Object value) {
        queryCondition.append(" > ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition greaterOrEqual(Object value) {
        queryCondition.append(" >= ").append(value.toString());
        return new ConditionImpl(queryCondition);
    }

}
