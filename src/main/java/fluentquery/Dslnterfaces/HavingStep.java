package fluentquery.Dslnterfaces;

public interface HavingStep extends EndQuery {
    EndQuery having(Condition condition);
}
