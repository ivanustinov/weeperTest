package ru.ustinov.sapertest.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.ustinov.sapertest.json.JsonUtil;
import ru.ustinov.sapertest.to.GameInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ustinov.sapertest.web.MinesWeeperTestData.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@PropertySource("classpath:validation.properties")
class MinesWeeperControllerTest extends AbstractControllerTest {

    public static final String NEW_URL = "/new";

    @Value("${valid.width.max}")
    public String maxWidth;

    @Value("${valid.width.min}")
    public String minWidth;

    @Value("${valid.height.max}")
    public String maxHeight;

    @Value("${valid.height.min}")
    public String minHeight;

    @Value("${valid.mines.min}")
    public String minMinesCount;

    @Value("${valid.freeField.min}")
    public String minCountOfFreeCells;

    @Test
    void startGame() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(NEW_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newGameRequest)))
                .andDo(print());
        // Получаем JSON строку из ответа
        final String contentAsString = action.andReturn().getResponse().getContentAsString();
        final GameInfoResponse gameInfoResponse = JsonUtil.readValue(contentAsString, GameInfoResponse.class);
        assertThat(gameInfoResponse).usingRecursiveComparison()
                .ignoringFields("gameId").isEqualTo(gameInfo);
    }

    @Test
    void invalidWidthStartGame() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(NEW_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MinesWeeperTestData.invalidWidthGameRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value(messageSourceAccessor
                                .getMessage("valid.width.interval", new String[]{minWidth, maxWidth})))
                .andDo(print());
    }

    @Test
    void invalidHeightStartGame() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(NEW_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MinesWeeperTestData.invalidHeightGameRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value(messageSourceAccessor
                                .getMessage("valid.height.interval", new String[]{minHeight, maxHeight})))
                .andDo(print());
    }

    @Test
    void invalidMinesCountStartGame() throws Exception {
        perform(MockMvcRequestBuilders.post(NEW_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidMineCountGameRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value(messageSourceAccessor
                                .getMessage("valid.mine_count.message", new String[]{minMinesCount, maxMinCountStr})))
                .andDo(print());
    }

//    @Test
//    void invalidMinecountStartGame() throws Exception {
//        final String minCountOfFreeCells = this.minCountOfFreeCells;
//        final int totalCells = invalidMineCountGameRequest.getHeight() * invalidMineCountGameRequest.getWidth();
//        final int i = i1 - minCountOfFreeCells;
//        ResultActions action = perform(MockMvcRequestBuilders.post(NEW_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(MinesWeeperTestData.invalidMineCountGameRequest)))
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(jsonPath("$.error")
//                        .value(messageSourceAccessor.getMessage(NEW_GAME_MINE_COUNT_CHECK, minMinesCount, )))
//                .andDo(print());
//    }

//    @Test
//    void turn() throws Exception {
//        // Создаем фиктивную сессию
//        MockHttpSession session = new MockHttpSession();
//        final GameTurnRequest gameTurnRequest = MinesWeeperTestData.firstTurnRequest;
//        // Замокируем поведение session.getAttribute(...)
//        final GameInfoResponse gameResponse = MinesWeeperTestData.getGame();
//        final String gameId = gameResponse.getGameId();
//        session.setAttribute(gameId, gameResponse);
//        gameTurnRequest.setGameId(gameId);
//        final String s = JsonUtil.writeValue(gameTurnRequest);
//        System.out.println(s);
//        ResultActions action = perform(MockMvcRequestBuilders.post(TURN_URL)
//                .session(session)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(s))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.game_id").value(gameId));
//    }
}