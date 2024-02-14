package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Data
@AllArgsConstructor
public class GameTurnRequest {

    @NotNull
    @Schema(description = "Колонка проверяемой ячейки (нумерация с нуля)", example = "5")
    private Integer col;

    @NotNull
    @Schema(description = "Ряд проверяемой ячейки (нумерация с нуля)", example = "5")
    private Integer row;

    @NotBlank
    @JsonProperty("game_id")
    @Schema(description = "Идентификатор игры", example = "01234567-89AB-CDEF-0123-456789ABCDEF")
    private String gameId;
}
