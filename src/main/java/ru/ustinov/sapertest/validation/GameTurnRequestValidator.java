package ru.ustinov.sapertest.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.ustinov.sapertest.exception.SaperException;
import ru.ustinov.sapertest.to.FieldEnum;

import static ru.ustinov.sapertest.SaperExceptionHandler.*;
import static ru.ustinov.sapertest.to.FieldEnum.SPACE;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@Component
public class GameTurnRequestValidator {


    /**
     * Проверка на выход за границы поля и на повторный ход
     * @param field игровое поле
     * @param row ряд хода
     * @param col колонка хода
     */
    public void turnCheck(FieldEnum[][] field, int row, int col) {
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
        } else if (field[row][col] != SPACE) {
            messageCode = TURN_CELL_OPENED;
            params = new String[]{};
        }
        if (messageCode != null) {
            throw new SaperException(HttpStatus.BAD_REQUEST, messageCode, params);
        }
    }

}
