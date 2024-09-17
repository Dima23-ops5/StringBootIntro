package mate.academy.StringBootIntro.exeption;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Throwable c) {
        super(message, c);
    }
}
