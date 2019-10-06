package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

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
