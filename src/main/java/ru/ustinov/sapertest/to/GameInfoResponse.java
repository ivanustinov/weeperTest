package ru.ustinov.sapertest.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ustinov.sapertest.json.CharArraySerializer;

import java.util.Random;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 09.02.2024
 */
@Data
@JsonPropertyOrder({"game_id", "width", "height", "mines_count", "field", "completed"})
@NoArgsConstructor
public class GameInfoResponse {

    @JsonProperty("game_id")
    @Schema(description = "ID игры", example = "01234567-89AB-CDEF-0123-456789ABCDEF")
    private String gameId;

    @Schema(description = "Ширина поля", example = "10")
    private int height;

    @Schema(description = "Высота поля", example = "10")
    private int width;

    @Schema(description = "Количество мин", example = "20")
    @JsonProperty("mines_count")
    private int minesCount;

    @JsonIgnore
    private char[][] marked;

    //Можно было сделать String[][] и не заморачиваться за CharArraySerislizer
    @Schema(description = "Игровое поле")
    @JsonProperty("field")
    @JsonSerialize(using = CharArraySerializer.class)
    private char[][] forPlayer;

    @JsonIgnore
    private int[][] coordinatesOfMines;

    @Schema(description = "Завершена ли игра")
    private boolean completed;

    @JsonIgnore
    private int countOfLeftCells;

    public GameInfoResponse(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.minesCount = mines;
        countOfLeftCells = height * width - mines;
        createFields(height, width, mines);
    }

    /**
     * Инициализация игрового поля в соответствии со входными параметрами
     * @param height высота поля
     * @param width ширина поля
     * @param mines количество мин
     */
    private void createFields(int height, int width, int mines) {
        this.marked = new char[height][width];
        this.forPlayer = new char[height][width];
        this.coordinatesOfMines = new int[mines][2];
        // Размещение мин
        Random random = new Random();
        for (int i = 0; i < mines; i++) {
            int row = random.nextInt(height), col = random.nextInt(width);
            // Проверяем, не установлена ли уже мина в этой ячейке
            if (this.marked[row][col] != 'X') {
                this.marked[row][col] = 'X';
                // Записываем координаты мин
                coordinatesOfMines[i][0] = row;
                coordinatesOfMines[i][1] = col;
            } else {
                // Если мина уже установлена, генерируем новые координаты
                i--;
            }
        }
        // Разметка клеток поля и создание представления для игрока
        final int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.forPlayer[i][j] = ' ';
                if (this.marked[i][j] != 'X') {
                    int count = countOfMinesAround(marked, directions, i, j);
                    // Маркируем ячейку в соответствии с количеством мин вокруг
                    this.marked[i][j] = Character.forDigit(count, 10);
                }
            }
        }

    }

    /**
     * Подсчет мин вокруг ячейки поля
     * @param field игровоее поле
     * @param directions окружающие ячейки
     * @param row ряд ячейки для подсчета
     * @param col клолнка ячейки для подсчета
     * @return количество мин вокруг ячейки
     */
    private int countOfMinesAround(char[][] field, int[][] directions, int row, int col) {
        int count = 0;
        int numRows = field.length, numCols = field[0].length;
        // Массив смещений для соседних ячеек
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            // Проверяем, что новые координаты находятся в пределах поля и клетка заминирована
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols && field[newRow][newCol] == 'X') {
                count++;
            }
        }
        return count;
    }
}
