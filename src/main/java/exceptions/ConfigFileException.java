package exceptions;

public class ConfigFileException extends RuntimeException {

    public ConfigFileException() {
    }

    public ConfigFileException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
