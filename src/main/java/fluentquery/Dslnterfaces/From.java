package fluentquery.Dslnterfaces;

public interface From {
    //NextQuery from(String table);
    AfterFromStep from(Table table);
}
