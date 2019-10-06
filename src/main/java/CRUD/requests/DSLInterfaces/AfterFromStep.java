package CRUD.requests.DSLInterfaces;

public interface AfterFromStep extends EndQuery {
    EndQuery orderBy(SimpleField column);

    HavingStep groupBy(SimpleField column);

    AfterFromStep where(String condition);

    AfterFromStep where(Condition condition);

    AfterFromStep where();
}
