package mate.academy.springbootintro.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordValidator
        implements ConstraintValidator<Password, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(
            UserRegistrationRequestDto userRegistrationRequestDto,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        org.passay.PasswordValidator validation = new org.passay.PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()

                ));
        RuleResult result = validation.validate(
                new PasswordData(userRegistrationRequestDto.getPassword())
        );
        return result.isValid();
    }
}
