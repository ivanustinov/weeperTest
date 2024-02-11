package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import ru.ustinov.sapertest.validation.ValidMinesCount;

/***
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Data
@ValidMinesCount
public class NewFieldTo {

    @Min(value = 2)
    @Max(value = 30)
    private byte width;

    @Min(value = 2)
    @Max(value = 30)
    private byte height;

    @JsonProperty("mines_count")
    private short minesCount;

}
