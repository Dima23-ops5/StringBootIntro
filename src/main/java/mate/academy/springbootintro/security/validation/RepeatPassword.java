package mate.academy.springbootintro.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RepeatPasswordValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatPassword {
    String message() default "Invalid repeat password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
