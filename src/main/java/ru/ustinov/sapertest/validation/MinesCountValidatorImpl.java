package ru.ustinov.sapertest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.to.NewFieldTo;

import java.util.Objects;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Component
@PropertySource("classpath:validation.properties")
public class MinesCountValidatorImpl implements ConstraintValidator<MinesCountCheck, NewFieldTo> {

    @Autowired
    private Environment env;

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    private String messageCode;

    @Override
    public void initialize(MinesCountCheck constraintAnnotation) {
        this.messageCode = constraintAnnotation.messageCode();
    }

    @Override
    public boolean isValid(NewFieldTo newFieldTo, ConstraintValidatorContext context) {
        final int minFreeField = Integer.parseInt(Objects.requireNonNull(env.getProperty("valid.freeField.min")));
        final int minMinesCount = Integer.parseInt(Objects.requireNonNull(env.getProperty("valid.mines.min")));
        final int maxCountMines = newFieldTo.getWidth() * newFieldTo.getHeight() - minFreeField;
        final Integer minesCount = newFieldTo.getMinesCount();
        final boolean isValid = minesCount >= minMinesCount && minesCount <= maxCountMines;
        if (!isValid) {
            // Получаем сообщение из файла свойств и заполняем параметры
            final Object[] params = {minMinesCount, maxCountMines};
            String message = messageSourceAccessor.getMessage(messageCode, params, LocaleContextHolder.getLocale());
            // Подставляем сообщение в контекст валидации
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }

}
