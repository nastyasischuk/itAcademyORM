package fluentquery.Dslnterfaces;

public interface JoinStep {
    AfterFromStep on(Condition condition);
    AfterFromStep using(SimpleField... fields);
    AfterFromStep as(SimpleField field);
}
