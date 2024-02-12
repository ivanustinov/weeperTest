package ru.ustinov.sapertest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ustinov.sapertest.exception.SaperException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 11.02.2024
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class SaperExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private final MessageSourceAccessor messageSourceAccessor;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult());
    }

    @ExceptionHandler(SaperException.class)
    public ResponseEntity<?> saperException(SaperException ex) {
        log.error("SessionExpieredException ", ex);
        final String messagCode = Objects.requireNonNull(ex.getReason());
        final String message = messageSourceAccessor.getMessage(messagCode, ex.getParams());
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result) {
        String[] msg = result.getAllErrors().stream().map(messageSourceAccessor::getMessage).toArray(String[]::new);
        Map<String, Object> body = new HashMap<>();
        StringBuilder message = new StringBuilder();
        for (String s : msg) {
            message.append(s).append("\n");
        }
        body.put("error", message.toString());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

}
