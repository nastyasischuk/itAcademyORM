package exceptions;

public class TransactionException extends RuntimeException {
    private String message;

    public TransactionException() {
    }

    public TransactionException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }
}