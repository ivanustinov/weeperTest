package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */

@Data
public class TurnTo {

    private byte col;

    private byte row;

    @JsonProperty("game_id")
    private String gameId;
}
