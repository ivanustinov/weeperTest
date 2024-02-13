package ru.ustinov.sapertest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.to.NewGameRequest;

import java.util.Objects;

/**
 * Валидация количества мин в зависимости от размеров поля и минимального
 * количества свободных ячеек
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Component
@PropertySource("classpath:validation.properties")
public class MinesCountValidatorImpl implements ConstraintValidator<MinesCountCheck, NewGameRequest> {

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
    public boolean isValid(NewGameRequest newGameRequest, ConstraintValidatorContext context) {
        final int minFreeField = Integer.parseInt(Objects.requireNonNull(env.getProperty("valid.freeField.min")));
        final int minMinesCount = Integer.parseInt(Objects.requireNonNull(env.getProperty("valid.mines.min")));
        final int maxCountMines = newGameRequest.getWidth() * newGameRequest.getHeight() - minFreeField;
        final Integer minesCount = newGameRequest.getMinesCount();
        final boolean isValid = minesCount >= minMinesCount && minesCount <= maxCountMines;
        if (!isValid) {
            // Получаем сообщение из файла свойств и заполняем параметры
            final String[] params = new String[]{String.valueOf(minMinesCount), String.valueOf(maxCountMines)};
            String message = messageSourceAccessor.getMessage(messageCode, params);
            // Подставляем сообщение в контекст валидации
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation().disableDefaultConstraintViolation();
        }
        return isValid;
    }

}
