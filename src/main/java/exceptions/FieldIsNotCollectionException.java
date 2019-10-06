package exceptions;

public class FieldIsNotCollectionException extends RuntimeException {
    public FieldIsNotCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldIsNotCollectionException(String message) {
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
