package ru.ustinov.sapertest.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.exception.SessionExpierdException;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@Component
public class TurnValidator {

    public void turnCheck(char[][] field, int row, int col) {
        final int rows = field.length;
        final int cols = field[0].length;
        turnRowCheck(rows, row);
        turnColCheck(cols, col);
        checkIsCellOpened(field, row, col);
    }

    public void turnRowCheck(int rows, int row) {
        if (row >= rows && row < 0) {
            throw new SessionExpierdException(HttpStatus.BAD_REQUEST, "valid.turn_row.message", String.valueOf(rows));
        }
    }

    public void turnColCheck(int cols, int col) {
        if (col >= cols && col < 0) {
            throw new SessionExpierdException(HttpStatus.BAD_REQUEST, "valid.turn_col.message", String.valueOf(cols));
        }
    }

    public void checkIsCellOpened(char[][] field, int row, int col) {
        if (field[row][col] != ' ') {
            throw new SessionExpierdException(HttpStatus.BAD_REQUEST, "valid.turn_cell_opened.message");
        }
    }

}
