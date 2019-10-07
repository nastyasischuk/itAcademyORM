package exceptions;

public class WrongColumnNameException extends RuntimeException {
    private String message;

    public WrongColumnNameException() {
    }

    public WrongColumnNameException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public WrongColumnNameException(String message) {
        super(message);
    }

    public WrongColumnNameException(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }
}
