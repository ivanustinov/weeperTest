package ru.ustinov.sapertest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ustinov.sapertest.model.Field;

import java.util.UUID;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Service
public class FieldService {

    private final ThreadLocal<int[][]> directionsThreadLocal = ThreadLocal.withInitial(() ->
            new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}});

    public int[][] getDirections() {
        return directionsThreadLocal.get();
    }

    public Field startGame(byte height, byte width, short mines) {
        final Field field = new Field(height, width, mines);
        field.setGameId(UUID.randomUUID().toString());
        return field;
    }

    public Field turn(Field field, int row, int col) {
        final char[][] marked = field.getMarked();
        char[][] forPlayer = field.getForPlayer();
        // Проверка на подрыв
        if (marked[row][col] == 'X') {
            log.info("Произошел подрыв на клетке с координатами x:{}, y:{} на поле с uuid = {}", row, col,
                    field.getGameId());
            field.setForPlayer(marked);
            field.setCompleted(true);
        } else {
            final int leftCells = openCell(marked, forPlayer, row, col, field.getCountOfLeftCells());
            if (leftCells == 0) {
                log.info("Окончание игры с победой!");
                field.setCompleted(true);
                // Открываем мины игроку после победы
                mapMineFieldForWinner(forPlayer, field.getCoordinatesOfMines());
            }
            field.setCountOfLeftCells(leftCells);
        }
        return field;
    }

    public int openCell(char[][] marked, char[][] forPlayer, int row, int col, int countOfLeftCells) {
        int numRows = marked.length, numCols = marked[0].length;
        // Проверка выхода за границы массива и что поле еще не открыто
        if (row >= 0 && row < numRows && col >= 0 && col < numCols && marked[row][col] != forPlayer[row][col]) {
            // Открываем ячейку
            forPlayer[row][col] = marked[row][col];
            // Убавляем счетчик оставшихся ячеек игрового поля
            countOfLeftCells--;
            // Если нет вокруг мин и игра не закончилась, начинаем рекурсивный обход соседних ячеек
            if (forPlayer[row][col] == '0' && countOfLeftCells != 0) {
                final int[][] directions = getDirections();
                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];
                    countOfLeftCells = openCell(marked, forPlayer, newRow, newCol, countOfLeftCells);
                    if (countOfLeftCells == 0) {
                        break;
                    }
                }
            }
        }
        return countOfLeftCells;
    }

    public void mapMineFieldForWinner(char[][] forPlayer, int[][] coordinatesOfMines) {
        for (int[] coordinatesOfMine : coordinatesOfMines) {
            final int row = coordinatesOfMine[0];
            final int col = coordinatesOfMine[1];
            forPlayer[row][col] = 'M';
        }
    }

}
