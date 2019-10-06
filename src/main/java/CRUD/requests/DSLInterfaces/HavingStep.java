package CRUD.requests.DSLInterfaces;

public interface HavingStep extends EndQuery {
    EndQuery having(Condition condition);
}
