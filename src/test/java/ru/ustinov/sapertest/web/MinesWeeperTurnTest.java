package ru.ustinov.sapertest.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.ustinov.sapertest.json.JsonUtil;
import ru.ustinov.sapertest.to.GameInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.ustinov.sapertest.web.MinesWeeperTurnTestData.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.02.2024
 */
public class MinesWeeperTurnTest extends AbstractControllerTest {

    public static final String TURN_URL = "/turn";

    private MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        // Создаем фиктивную сессию
        httpSession = new MockHttpSession();
        // Замокируем поведение session.getAttribute(...)
        final String gameId = game.getGameId();
        final int height = game.getHeight();
        final int width = game.getWidth();
        final int minesCount = game.getMinesCount();
        final GameInfoResponse testInfo = new GameInfoResponse(height, width, minesCount);
        testInfo.setMarked(marked);
        testInfo.setGameId(gameId);
        httpSession.setAttribute(gameId, testInfo);
    }

    @Test
    void turnWithNumber() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(turnWithNumber)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        game.setForPlayer(forPlayerTurnWithNumber);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

    @Test
    void turnWithZero() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(turnWithZero)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        game.setForPlayer(forPlayerTurnWithZero);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

    @Test
    void winnerTurn() throws Exception {
        final GameInfoResponse gameInfoResponse = (GameInfoResponse) httpSession.getAttribute(gameId);
        gameInfoResponse.setForPlayer(forPlayerTurnWithZero);
        gameInfoResponse.setCountOfLeftCells(1);
        gameInfoResponse.setCoordinatesOfMines(game.getCoordinatesOfMines());
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(winnerTurn)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        game.setForPlayer(forPlayerWinnerTurn);
        game.setCompleted(true);
        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse winnerGameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        assertThat(winnerGameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

    @Test
    void loserTurn() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loserTurn)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        game.setForPlayer(forPlayerLoserTurn);
        game.setCompleted(true);
        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

    @Test
    void repeatedTurn() throws Exception {
        final GameInfoResponse gameInfoResponse = (GameInfoResponse) httpSession.getAttribute(gameId);
        gameInfoResponse.setForPlayer(forPlayerTurnWithNumber);
        perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(repeatedTurn)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error").value(messageSourceAccessor
                        .getMessage("valid.turn_cell_opened.message")));

    }

    @Test
    void turnOutOfColBorder() throws Exception {
        perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(turnColOutOfBorder)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error").value(messageSourceAccessor
                        .getMessage("valid.turn_col.message", new String[]{String.valueOf(game.getHeight())})));

    }

    @Test
    void turnOutOfRowBorder() throws Exception {
        perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(turnRowOutOfBorder)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error").value(messageSourceAccessor
                        .getMessage("valid.turn_row.message", new String[]{String.valueOf(game.getWidth())})));

    }


}
