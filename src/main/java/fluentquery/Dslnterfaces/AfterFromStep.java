package fluentquery.Dslnterfaces;

public interface AfterFromStep extends EndQuery {
    Ordering orderBy(SimpleField column);

    HavingStep groupBy(SimpleField column);

    AfterFromStep where(String condition);

    AfterFromStep where(Condition condition);

    AfterFromStep where();

    JoinStep rightJoin(Table table);
    JoinStep innerJoin(Table table);
    JoinStep leftJoin(Table table);
    JoinStep join(Table table);
}
