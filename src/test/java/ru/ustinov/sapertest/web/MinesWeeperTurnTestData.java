package ru.ustinov.sapertest.web;

import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.to.GameTurnRequest;

import java.util.UUID;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.02.2024
 */
public class MinesWeeperTurnTestData {

    public static final String gameId = UUID.randomUUID().toString();

    public static final GameInfoResponse game = new GameInfoResponse(3, 3, 2);

    public static final GameTurnRequest turnInBorder = new GameTurnRequest(1, 1, gameId);
    public static final GameTurnRequest turnOutOfBorder = new GameTurnRequest(3, 3, gameId);
    public static final GameTurnRequest repeatedTurn = new GameTurnRequest(1, 1, gameId);
//    public static final GameTurnRequest turnWithZero = new GameTurnRequest(2, 2, gameId);
    public static final GameTurnRequest winnerTurn = new GameTurnRequest(0, 2, gameId);
    public static final GameTurnRequest loserTurn = new GameTurnRequest(0, 0, gameId);

    public static final int[][] coordinatesOfMInes = {{0, 0}, {0,1}};

    public static final char [][] field = {{'X', '2', '0'}, {'X', '2', '0'}, {'1', '1', '0'}};
    public static final char [][] forPlayerTurnWithNumber = {{' ', ' ', ' '}, {' ', '2', ' '}, {' ', ' ', ' '}};
    public static final char [][] forPlayerTurnWithZero = {{' ', '2', '0'}, {' ', '2', '0'}, {' ', '1', '0'}};
    public static final char [][] forPlayerWinnerTurn = {{'M', '2', '0'}, {'M', '2', '0'}, {'1', '1', '0'}};
    public static final char [][] forPlayerLoserTurn = {{'X', '2', '0'}, {'X', '2', '0'}, {'1', '1', '0'}};

    static {
        game.setGameId(gameId);
        game.setMarked(field);
    }

}
