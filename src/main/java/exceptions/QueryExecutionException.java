package exceptions;

public class QueryExecutionException extends RuntimeException {
    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
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
