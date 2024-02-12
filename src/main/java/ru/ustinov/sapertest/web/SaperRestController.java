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
import ru.ustinov.sapertest.model.GameInfoResponse;
import ru.ustinov.sapertest.service.FieldService;
import ru.ustinov.sapertest.to.NewGameRequest;
import ru.ustinov.sapertest.to.GameTurnRequest;

import java.util.UUID;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Tag(name = "Minesweeper")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SaperRestController {

    @Autowired
    private FieldService fieldService;

    @Operation(summary = "Начало новой игры")
    @PostMapping("/new")
    public GameInfoResponse startGame(@Valid @RequestBody NewGameRequest newGameRequest, HttpSession session) {
        final GameInfoResponse gameInfoResponse = new GameInfoResponse(newGameRequest.getHeight()
                , newGameRequest.getWidth(), newGameRequest.getMinesCount());
        gameInfoResponse.setGameId(UUID.randomUUID().toString());
        // Сохраняем объект GameInfoResponse в сессии
        session.setAttribute(gameInfoResponse.getGameId(), gameInfoResponse);
        if (session.isNew()) {
            // Устанавливаем время сессии 1 минуту(60 секунд)
            session.setMaxInactiveInterval(60);
        }
        return gameInfoResponse;
    }

    @Operation(summary = "Ход пользователя")
    @PostMapping("/turn")
    public GameInfoResponse turn(@Valid @RequestBody GameTurnRequest gameTurnRequest, HttpSession session) {
        final String gameId = gameTurnRequest.getGameId();
        GameInfoResponse gameInfoResponse = (GameInfoResponse) session.getAttribute(gameId);
        if (gameInfoResponse == null) {
            throw new SaperException(HttpStatus.BAD_REQUEST, "session_expired.message", gameId);
        }
        return fieldService.turn(gameInfoResponse, gameTurnRequest.getRow(), gameTurnRequest.getCol());
    }

}
