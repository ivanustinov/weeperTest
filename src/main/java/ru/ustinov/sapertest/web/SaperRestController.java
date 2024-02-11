package ru.ustinov.sapertest.web;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.ustinov.sapertest.exception.SessionExpierdException;
import ru.ustinov.sapertest.model.Field;
import ru.ustinov.sapertest.service.FieldService;
import ru.ustinov.sapertest.to.NewFieldTo;
import ru.ustinov.sapertest.to.TurnTo;

import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SaperRestController {

    @Autowired
    private FieldService fieldService;


    @PostMapping("/new")
    public Field startGame(@Valid @RequestBody NewFieldTo fieldTo, HttpSession session) {
        final Field field = fieldService.startGame(fieldTo.getHeight(), fieldTo.getWidth(), fieldTo.getMinesCount());
        // Сохраняем объект Field в сессии
        session.setAttribute(field.getGameId(), field);
        // Устанавливаем время сессии 1 минуту(60 секунд)
        session.setMaxInactiveInterval(60);
        return field;
    }

    @PostMapping("/turn")
    public Field turn(@Valid @RequestBody TurnTo turnTo, HttpSession session) {
        final String gameId = turnTo.getGameId();
        Field field = (Field) session.getAttribute(gameId);
        if (field == null) {
            throw new SessionExpierdException(HttpStatus.BAD_REQUEST,
                    String.format("игра с идентификатором %s не была создана или устарела (неактуальна)", gameId));
        }
        return fieldService.turn(field, turnTo.getRow(), turnTo.getCol());
    }
}
