package mate.academy.springbootintro.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;

public class RepeatPasswordValidator
        implements ConstraintValidator<RepeatPassword, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(
            UserRegistrationRequestDto userRegistrationRequestDto,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return userRegistrationRequestDto.getPassword()
                .equals(userRegistrationRequestDto.getRepeatPassword());
    }
}
