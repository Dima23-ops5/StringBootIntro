package mate.academy.springbootintro.dto.userdto;

import mate.academy.springbootintro.security.validation.Email;
import mate.academy.springbootintro.security.validation.Password;

public record UserLoginRequestDto(@Email String email, @Password String password) {
}
