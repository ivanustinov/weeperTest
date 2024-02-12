package ru.ustinov.sapertest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 11.02.2024
 */
@Getter
public class SaperException extends ResponseStatusException {

    private final String[] params;

    public SaperException(HttpStatus status, String message, String... params) {
        super(status, message);
        this.params = params;
    }

}
