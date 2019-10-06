package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class SkippableFieldImpl extends SimpleFieldImpl implements SkippableField {
    private StringBuilder queryCondition;

    SkippableFieldImpl() {
        super("");
        queryCondition = new StringBuilder();
    }

    @Override
    public Condition exists(AfterFromStep selectFrom) {
        queryCondition.append(" exists ").append(" (").append(selectFrom.toString()).append(") ");
        return new ConditionImpl(queryCondition);
    }

    @Override
    public Condition notExists(AfterFromStep selectFrom) {
        queryCondition.append(" not exists ").append(" (").append(selectFrom.toString()).append(") ");
        return new ConditionImpl(queryCondition);
    }
}
