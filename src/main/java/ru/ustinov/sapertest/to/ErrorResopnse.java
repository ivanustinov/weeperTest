package ru.ustinov.sapertest.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.02.2024
 */
@Data
public class ErrorResopnse {

    @Schema(description = "Описание ошибки", example = "Произошла непредвиденная ошибка")
    private String error;

    public ErrorResopnse(String error) {
        this.error = error;
    }

}
