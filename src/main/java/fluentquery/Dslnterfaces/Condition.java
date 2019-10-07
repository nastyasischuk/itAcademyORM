package fluentquery.Dslnterfaces;

public interface Condition {
    Condition and(Condition condition);
    Condition or(Condition condition);
    Condition andNot(Condition condition);
    Condition orNot(Condition condition);
    Condition not();
}
