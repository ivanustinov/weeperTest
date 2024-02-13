package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.ustinov.sapertest.validation.MinesCountCheck;

import static ru.ustinov.sapertest.SaperExceptionHandler.NEW_GAME_MINE_COUNT_CHECK;

/***
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Data
@MinesCountCheck(messageCode = NEW_GAME_MINE_COUNT_CHECK)
@AllArgsConstructor
@NoArgsConstructor
public class NewGameRequest {

    // Если поменять диапазон здесь, но не синхронизовать с validation.properties упадет тест
    @NotNull
    @Range(min = 2, max = 30, message = "ширина поля должна быть не менее {min} и не более {max}")
    @Schema(description = "Ширина игрового поля", example = "10")
    private Integer width;

    // Если поменять диапазон здесь, но не синхронизовать с validation.properties упадет тест
    @NotNull
    @Range(min = 2, max = 30, message = "высота поля должна быть не менее {min} и не более {max}")
    @Schema(description = "Высота игрового поля", example = "10")
    private Integer height;

    @NotNull
    @JsonProperty("mines_count")
    @Schema(description = "Количество мин на поле", example = "10")
    private Integer minesCount;

}
