package exceptions;

public class SeveralPrimaryKeysException extends Exception{
    public SeveralPrimaryKeysException() {
        super("Several primary keys");
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
