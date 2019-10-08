package exceptions;

public class WrongFieldException extends RuntimeException{
    public WrongFieldException() {
        super("Cannot build a table without primary key");
    }

    public WrongFieldException(String message) {
        super("Wrong field name"+message);
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
