package exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
