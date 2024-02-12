package ru.ustinov.sapertest.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ustinov.sapertest.exception.SessionExpierdException;
import ru.ustinov.sapertest.model.Field;
import ru.ustinov.sapertest.service.FieldService;
import ru.ustinov.sapertest.to.NewFieldTo;
import ru.ustinov.sapertest.to.TurnTo;
import ru.ustinov.sapertest.validation.TurnValidator;

import java.util.UUID;

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

    @Autowired
    private TurnValidator turnValidator;

    @PostMapping("/new")
    public Field startGame(@Valid @RequestBody NewFieldTo fieldTo, HttpSession session) {
        final Field field = new Field(fieldTo.getHeight(), fieldTo.getWidth(), fieldTo.getMinesCount());
        field.setGameId(UUID.randomUUID().toString());
        // Сохраняем объект Field в сессии
        session.setAttribute(field.getGameId(), field);
        if (session.isNew()) {
            // Устанавливаем время сессии 1 минуту(60 секунд)
            session.setMaxInactiveInterval(60);
        }
        return field;
    }

    @PostMapping("/turn")
    public Field turn(@Valid @RequestBody TurnTo turnTo, HttpSession session) {
        final String gameId = turnTo.getGameId();
        // Получаем сессию из объекта запроса;
        Field field = (Field) session.getAttribute(gameId);
        if (field == null) {
            throw new SessionExpierdException(HttpStatus.BAD_REQUEST, "session_expired.message", gameId);
        }
        return fieldService.turn(field, turnTo.getRow(), turnTo.getCol());
    }

}
