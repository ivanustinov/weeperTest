package ru.ustinov.sapertest.web;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeAll;
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
import static ru.ustinov.sapertest.web.MinesWeeperTurnTestData.game;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.02.2024
 */
public class MinesWeeperTurnTest extends AbstractControllerTest {

    public static final String TURN_URL = "/turn";

    private MockHttpSession httpSession;

    @PostConstruct
    public void initSession() {
        // Создаем фиктивную сессию
        httpSession = new MockHttpSession();
        // Замокируем поведение session.getAttribute(...)
        final GameInfoResponse gameResponse = MinesWeeperTurnTestData.game;
        final String gameId = gameResponse.getGameId();
        httpSession.setAttribute(gameId, gameResponse);
    }

    @Test
    void turn() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MinesWeeperTurnTestData.turnInBorder)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Получаем JSON строку из ответа
        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

    @Test
    void winnerTurn() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MinesWeeperTurnTestData.turnInBorder)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Получаем JSON строку из ответа
        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("marked", "countOfLeftCells", "coordinatesOfMines").isEqualTo(game);
    }

}
