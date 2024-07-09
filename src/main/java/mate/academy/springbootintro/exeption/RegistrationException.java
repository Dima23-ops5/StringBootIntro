package mate.academy.springbootintro.exeption;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message, Throwable ex) {
        super(message, ex);
    }
}
