package ru.ustinov.sapertest.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.exception.SaperException;

import static ru.ustinov.sapertest.SaperExceptionHandler.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@Component
public class GameTurnRequestValidator {

    public void turnCheck(char[][] field, int row, int col) {
        final int rows = field.length;
        final int cols = field[0].length;
        String messageCode = null;
        String[] params = null;
        if (row >= rows || row < 0) {
            messageCode = TURN_ROW_ERROR;
            params = new String[]{String.valueOf(rows)};
        } else if (col >= cols || col < 0) {
            messageCode = TURN_COL_ERROR;
            params = new String[]{String.valueOf(cols)};
        } else if (field[row][col] != ' ') {
            messageCode = TURN_CELL_OPENED;
            params = new String[]{};
        }
        if (messageCode != null) {
            throw new SaperException(HttpStatus.BAD_REQUEST, messageCode, params);
        }
    }

}
