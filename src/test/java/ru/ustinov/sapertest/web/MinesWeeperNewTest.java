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
import static ru.ustinov.sapertest.web.MinesWeeperNewTestData.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@PropertySource("classpath:validation.properties")
class MinesWeeperNewTest extends AbstractControllerTest {

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
                .content(JsonUtil.writeValue(MinesWeeperNewTestData.invalidWidthGameRequest)))
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
                .content(JsonUtil.writeValue(MinesWeeperNewTestData.invalidHeightGameRequest)))
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

}