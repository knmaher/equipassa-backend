package com.equipassa.equipassa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid password. It must be at least 8 characters long, contain uppercase and lowercase letters, a digit, and a special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
