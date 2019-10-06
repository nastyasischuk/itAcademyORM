package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class EndQueryImpl implements EndQuery {
    private StringBuilder query;

    EndQueryImpl(StringBuilder query) {
        this.query = query;
    }

    @Override
    public String end() {
        return query.append(";").toString();
    }
}
