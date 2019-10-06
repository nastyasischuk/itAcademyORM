package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class HavingStepImpl implements HavingStep {
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
}
