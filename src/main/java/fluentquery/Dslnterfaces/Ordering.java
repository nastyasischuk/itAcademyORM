package fluentquery.Dslnterfaces;

public interface Ordering extends EndQuery {
    EndQuery asc();
    EndQuery desc();
}
