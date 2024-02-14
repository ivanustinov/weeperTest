package ru.ustinov.sapertest.service;

import ru.ustinov.sapertest.to.GameInfoResponse;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.02.2024
 */
public interface TurnService {

    GameInfoResponse turn(GameInfoResponse gameInfoResponse, int row, int col);

}
