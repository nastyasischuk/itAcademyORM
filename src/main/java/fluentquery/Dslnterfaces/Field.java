package fluentquery.Dslnterfaces;

public interface Field extends SimpleField {
    Condition isNull();

    Condition isNotNull();

    Condition like(String like);

    Condition notLike(String notLike);

    Condition in(AfterFromStep selectFrom);

    Condition notIn(AfterFromStep selectFrom);

    Condition between(Object first, Object last);

    Condition equal(Object value);

    Condition notEqual(Object value);

    Condition less(Object value);

    Condition lessOrEqual(Object value);

    Condition greater(Object value);

    Condition greaterOrEqual(Object value);


}
