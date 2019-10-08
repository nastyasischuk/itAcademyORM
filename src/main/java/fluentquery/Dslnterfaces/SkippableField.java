package fluentquery.Dslnterfaces;

public interface SkippableField extends SimpleField {
    Condition exists(AfterFromStep selectFrom);
    Condition notExists(AfterFromStep selectFrom);
}
