package mate.academy.springbootintro.dto.userdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.springbootintro.security.validation.Email;
import mate.academy.springbootintro.security.validation.Password;
import mate.academy.springbootintro.security.validation.RepeatPassword;

@Data
@RepeatPassword
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @Password
    private String password;
    private String repeatPassword;
    @NotBlank(message = "First name can`t be null.")
    private String firstName;
    @NotBlank(message = "Last name can`t be null.")
    private String lastName;
    @NotBlank(message = "Shipping address can`t be null.")
    private String shippingAddress;
}
