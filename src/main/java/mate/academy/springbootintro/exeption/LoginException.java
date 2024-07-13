package mate.academy.springbootintro.exeption;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
