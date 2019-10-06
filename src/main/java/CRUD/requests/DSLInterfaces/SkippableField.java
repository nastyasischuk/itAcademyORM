package CRUD.requests.DSLInterfaces;

public interface SkippableField extends SimpleField {
    Condition exists(AfterFromStep selectFrom);
    Condition notExists(AfterFromStep selectFrom);
}
