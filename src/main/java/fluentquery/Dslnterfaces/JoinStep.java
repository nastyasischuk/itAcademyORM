package fluentquery.Dslnterfaces;

public interface JoinStep {
    //todo add methods
    AfterFromStep on(Condition condition);
    AfterFromStep using(SimpleField... fields);
    AfterFromStep as(SimpleField field);
}
