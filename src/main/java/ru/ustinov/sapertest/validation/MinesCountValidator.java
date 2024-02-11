package ru.ustinov.sapertest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.to.NewFieldTo;

import java.util.Objects;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Component
@PropertySource("classpath:validation.properties")
public class MinesCountValidator implements ConstraintValidator<ValidMinesCount, NewFieldTo> {

    @Autowired
    private Environment env;

    @Override
    public void initialize(ValidMinesCount constraintAnnotation) {
    }

    @Override
    public boolean isValid(NewFieldTo newFieldTo, ConstraintValidatorContext context) {
        final int minMines = Integer.parseInt(Objects.requireNonNull(env.getProperty("mines.min")));
        final int minFreeField = Integer.parseInt(Objects.requireNonNull(env.getProperty("freeField.min")));
        return newFieldTo.getMinesCount() >= minMines &&
                newFieldTo.getMinesCount() <= newFieldTo.getWidth() * newFieldTo.getHeight() - minFreeField;
    }
}
