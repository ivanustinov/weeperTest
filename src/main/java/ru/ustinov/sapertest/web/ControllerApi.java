package ru.ustinov.sapertest.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ustinov.sapertest.to.ErrorResopnse;
import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.to.GameTurnRequest;
import ru.ustinov.sapertest.to.NewGameRequest;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.02.2024
 */
@Tag(name = "Minesweeper")
public interface ControllerApi {

    @Operation(summary = "Начало новой игры", responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Ошибка запроса или некорректное действие"
            , content = @Content(schema = @Schema(implementation = ErrorResopnse.class)))
    })
    GameInfoResponse startGame(NewGameRequest request, HttpSession session);

    @Operation(summary = "Ход пользователя", responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Ошибка запроса или некорректное действие"
            , content = @Content(schema = @Schema(implementation = ErrorResopnse.class)))
    })
    GameInfoResponse turn(GameTurnRequest request, HttpSession session);
}
