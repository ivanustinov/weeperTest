package ru.ustinov.sapertest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ustinov.sapertest.exception.SessionExpierdException;

import java.util.*;
import java.util.function.Function;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 11.02.2024
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class SaperExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes;

    @Autowired
    private final MessageSourceAccessor messageSourceAccessor;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @ExceptionHandler(SessionExpierdException.class)
    public ResponseEntity<?> sessionExpierdException(SessionExpierdException ex) {
        log.error("SessionExpieredException ", ex);
        final String messagCode = Objects.requireNonNull(ex.getReason());
        final String message = messageSourceAccessor.getMessage(messagCode, ex.getParams());
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String[] msg = result.getAllErrors().stream().map(new Function<ObjectError, String>() {
            @Override
            public String apply(ObjectError resolvable) {
                return messageSourceAccessor.getMessage(resolvable);
            }
        }).toArray(String[]::new);
        Map<String, Object> body = new HashMap<>();
        StringBuilder message = new StringBuilder();
        for (String s : msg) {
            message.append(s + "\n");
        }
        body.put("error", message.toString());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

}
