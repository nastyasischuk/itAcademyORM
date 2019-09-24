package exceptions;

public class NoPrimaryKeyException extends RuntimeException{
    public NoPrimaryKeyException() {
        super("Cannot build a table without primary key");
    }

    public NoPrimaryKeyException(String message) {
        super(message);
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
