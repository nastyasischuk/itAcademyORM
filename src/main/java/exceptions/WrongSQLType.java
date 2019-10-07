package exceptions;

public class WrongSQLType extends Exception {
    public WrongSQLType(Class<?> type) {
        super("Cannot build a column of type "+type);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
