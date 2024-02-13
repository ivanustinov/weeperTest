package ru.ustinov.sapertest.web;

import lombok.Getter;
import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.to.GameTurnRequest;
import ru.ustinov.sapertest.to.NewGameRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 12.02.2024
 */
@Getter
public class MinesWeeperNewTestData {

    // Статическая переменная для хранения свойств
    private static Properties properties;

    public static NewGameRequest newGameRequest;
    public static NewGameRequest invalidWidthGameRequest;
    public static NewGameRequest invalidHeightGameRequest;
    public static NewGameRequest invalidMineCountGameRequest;

    public static final String gameId = UUID.randomUUID().toString();

    public static String maxMinCountStr;

    public static final GameTurnRequest openedTurnRequest = new GameTurnRequest(0, 0, gameId);
    public static final GameTurnRequest outOfWidhtTurnRequest = new GameTurnRequest(0, 3, gameId);
    public static final GameTurnRequest outOfHeightTurnRequest = new GameTurnRequest(3, 0, gameId);

    public static final GameInfoResponse gameInfo = new GameInfoResponse();

    public static final char [][] forPlayerStart;


    // Статический блок инициализации для загрузки свойств из файла
    static {
        try {
            // Создаем объект Properties
            properties = new Properties();
            // Загружаем файл свойств
            InputStream inputStream = new FileInputStream("src/main/resources/validation.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String maxWidth = properties.getProperty("valid.width.max");
        final String minWidth = properties.getProperty("valid.width.min");
        final String maxHeight = properties.getProperty("valid.height.max");
        final String minHeight = properties.getProperty("valid.height.min");
        final String minFreeField = properties.getProperty("valid.freeField.min");
        final String minMineCount = properties.getProperty("valid.mines.min");

        final int width = Integer.parseInt(maxWidth);
        final int height = Integer.parseInt(maxHeight);
        final int minesCount = Integer.parseInt(minMineCount);
        final int totalCells = width * height;
        final int maxMinCount = totalCells - Integer.parseInt(minFreeField);
        maxMinCountStr = String.valueOf(maxMinCount);
        final int minWidthInt = Integer.parseInt(minWidth);
        final int minHeightInt = Integer.parseInt(minHeight);
        newGameRequest = new NewGameRequest(minWidthInt, minHeightInt, minesCount);
        invalidWidthGameRequest = new NewGameRequest(width + 1, height, minesCount);
        invalidHeightGameRequest = new NewGameRequest(width, height + 1, minesCount);
        invalidMineCountGameRequest = new NewGameRequest(width, height, maxMinCount + 1);

        forPlayerStart = new char[minHeightInt][minWidthInt];
        for (int i = 0; i < minHeightInt; i++) {
            for (int j = 0; j < minWidthInt; j++) {
                forPlayerStart[i][j] = ' ';
            }
        }
        gameInfo.setGameId(gameId);
        gameInfo.setHeight(newGameRequest.getHeight());
        gameInfo.setWidth(newGameRequest.getWidth());
        gameInfo.setMinesCount(newGameRequest.getMinesCount());;
        gameInfo.setForPlayer(forPlayerStart);

    }

}
