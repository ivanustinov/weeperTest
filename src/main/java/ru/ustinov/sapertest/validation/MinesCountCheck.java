package ru.ustinov.sapertest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Documented
@Constraint(validatedBy = MinesCountValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinesCountCheck {

    String message() default "";

    @NotNull(message = "Message code must be provided")
    String messageCode() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
