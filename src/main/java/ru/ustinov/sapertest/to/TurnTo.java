package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Data
public class TurnTo {

    @NotNull
    private Integer col;

    @NotNull
    private Integer row;

    @NotBlank
    @JsonProperty("game_id")
    private String gameId;
}
