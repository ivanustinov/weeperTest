package ru.ustinov.sapertest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.validation.GameTurnRequestValidator;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Slf4j
@Service
@AllArgsConstructor
public class FieldService {

    @Autowired
    private final GameTurnRequestValidator gameTurnRequestValidator;

    private final ThreadLocal<int[][]> directionsThreadLocal = ThreadLocal.withInitial(() ->
            new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}});

    public int[][] getDirections() {
        return directionsThreadLocal.get();
    }

    /**
     * Ход ирока
     * @param gameInfoResponse возвращаемое значение сущности текущей игры
     * @param row значение ряда хода
     * @param col значение колонки хода
     * @return текущее состояне игры после хода
     */
    public GameInfoResponse turn(GameInfoResponse gameInfoResponse, int row, int col) {
        gameTurnRequestValidator.turnCheck(gameInfoResponse.getForPlayer(), row, col);
        final char[][] marked = gameInfoResponse.getMarked();
        char[][] forPlayer = gameInfoResponse.getForPlayer();
        // Проверка на подрыв
        if (marked[row][col] == 'X') {
            log.info("Окончание игры с uuid = {}, подрыв на клетке с координатами строка:{}, столбец:{}"
                    , gameInfoResponse.getGameId(), row, col);
            gameInfoResponse.setForPlayer(marked);
            gameInfoResponse.setCompleted(true);
        } else {
            final int leftCells = openCell(marked, forPlayer, row, col, gameInfoResponse.getCountOfLeftCells());
            if (leftCells == 0) {
                log.info("Окончание игры с победой!");
                // Открываем мины игроку после победы
                mapMineFieldForWinner(forPlayer, gameInfoResponse.getCoordinatesOfMines());
            }
            gameInfoResponse.setCompleted(leftCells == 0);
            gameInfoResponse.setCountOfLeftCells(leftCells);
        }
        return gameInfoResponse;
    }

    /**
     * Рекурсивное открытие ячеек
     * @param marked размеченное поле
     * @param forPlayer поле для отображения игроку
     * @param row строка хода
     * @param col колонка хода
     * @param countOfLeftCells оставшиеся неоткрытые незаминированные клетки
     * @return количество оставшихся незаминированныъ ячеек
     */
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

    /**
     * Открытие заминированных ячеек при выигрыше
     * @param forPlayer отображение поля для игрока
     * @param coordinatesOfMines координаты заминированных ячеек
     */
    public void mapMineFieldForWinner(char[][] forPlayer, int[][] coordinatesOfMines) {
        for (int[] coordinatesOfMine : coordinatesOfMines) {
            final int row = coordinatesOfMine[0];
            final int col = coordinatesOfMine[1];
            forPlayer[row][col] = 'M';
        }
    }

}
