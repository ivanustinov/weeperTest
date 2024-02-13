package ru.ustinov.sapertest.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.ustinov.sapertest.exception.SaperException;
import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.service.FieldService;
import ru.ustinov.sapertest.to.NewGameRequest;
import ru.ustinov.sapertest.to.GameTurnRequest;

import java.util.UUID;

import static ru.ustinov.sapertest.SaperExceptionHandler.SESSION_EXPIRED;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Tag(name = "Minesweeper")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MinesWeeperController {

    @Autowired
    private FieldService fieldService;

    @Operation(summary = "Начало новой игры")
    @PostMapping("/new")
    public GameInfoResponse startGame(@Valid @RequestBody NewGameRequest request, HttpSession session) {
        final GameInfoResponse responce = new GameInfoResponse(request.getHeight()
                , request.getWidth(), request.getMinesCount());
        responce.setGameId(UUID.randomUUID().toString());
        log.info("В сессию c id = {} сохраняется новое поле с uuid = {}", session.getId(), responce.getGameId());
        // Сохраняем объект GameInfoResponse в сессии
        session.setAttribute(responce.getGameId(), responce);
        if (session.isNew()) {
            // Устанавливаем время сессии 1 минуту(60 секунд)
            session.setMaxInactiveInterval(60);
        }
        return responce;
    }

    @Operation(summary = "Ход пользователя")
    @PostMapping("/turn")
    public GameInfoResponse turn(@Valid @RequestBody GameTurnRequest request, HttpSession session) {
        final String gameId = request.getGameId();
        log.info("Новый ход в игре с uuid = {} ряд: {} колонка {}", request.getGameId(), request.getRow(), request.getCol());
        GameInfoResponse gameInfoResponse = (GameInfoResponse) session.getAttribute(gameId);
        if (gameInfoResponse == null) {
            throw new SaperException(HttpStatus.BAD_REQUEST, SESSION_EXPIRED, gameId);
        }
        return fieldService.turn(gameInfoResponse, request.getRow(), request.getCol());
    }

}
