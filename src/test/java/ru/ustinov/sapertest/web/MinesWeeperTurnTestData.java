package ru.ustinov.sapertest.web;

import ru.ustinov.sapertest.to.FieldEnum;
import ru.ustinov.sapertest.to.GameInfoResponse;
import ru.ustinov.sapertest.to.GameTurnRequest;

import java.util.UUID;

import static ru.ustinov.sapertest.to.FieldEnum.*;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.02.2024
 */
public class MinesWeeperTurnTestData {

    public static final String gameId = UUID.randomUUID().toString();

    public static final GameInfoResponse game = new GameInfoResponse(3, 3, 2);

    public static final GameTurnRequest turnWithNumber = new GameTurnRequest(1, 1, gameId);
    public static final GameTurnRequest turnColOutOfBorder = new GameTurnRequest(3, 1, gameId);
    public static final GameTurnRequest turnRowOutOfBorder = new GameTurnRequest(1, 3, gameId);
    public static final GameTurnRequest repeatedTurn = new GameTurnRequest(1, 1, gameId);
    public static final GameTurnRequest turnWithZero = new GameTurnRequest(2, 2, gameId);
    public static final GameTurnRequest winnerTurn = new GameTurnRequest(0, 2, gameId);
    public static final GameTurnRequest loserTurn = new GameTurnRequest(0, 0, gameId);

    public static final int[][] coordinatesOfMInes = {{0, 0}, {1, 0}};

    public static final FieldEnum[][] marked = {{X, _2, _0}, {X, _2, _0}, {_1, _1, _0}};
    public static final FieldEnum [][] forPlayerTurnWithNumber = {{SPACE, SPACE, SPACE}, {SPACE, _2, SPACE}, {SPACE, SPACE, SPACE}};
    public static final FieldEnum [][] forPlayerTurnWithZero = {{SPACE, _2, _0}, {SPACE, _2, _0}, {SPACE, _1, _0}};
    public static final FieldEnum [][] forPlayerWinnerTurn = {{M, _2, _0}, {M, _2, _0}, {_1, _1, _0}};
    public static final FieldEnum [][] forPlayerLoserTurn = {{X, _2, _0}, {X, _2, _0}, {_1, _1, _0}};

    static {
        game.setGameId(gameId);
        game.setCoordinatesOfMines(coordinatesOfMInes);
    }

}
