package ru.ustinov.sapertest.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.02.2024
 */
@Data
public class ErrorResopnse {

    @Schema(description = "Описание ошибки", example = "Произошла ошибка")
    private String error;

    public ErrorResopnse(String error) {
        this.error = error;
    }

}
