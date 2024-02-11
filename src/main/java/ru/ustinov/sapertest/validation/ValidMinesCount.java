package ru.ustinov.sapertest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Documented
@Constraint(validatedBy = MinesCountValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMinesCount {
    String message() default "Mines count must be between {0} and {1}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
