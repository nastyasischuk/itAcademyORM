package exceptions;

public class OpenConnectionException extends RuntimeException {

    public OpenConnectionException() {
    }

    public OpenConnectionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
