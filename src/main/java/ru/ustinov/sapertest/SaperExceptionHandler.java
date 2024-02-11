package ru.ustinov.sapertest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ustinov.sapertest.exception.SessionExpierdException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @ExceptionHandler(SessionExpierdException.class)
    public ResponseEntity<?> sessionExpierdException(SessionExpierdException ex) {
        log.error("EntityNotFoundException ", ex);
        return createResponseEntity(ex.getReason());
    }

    private <T> ResponseEntity<T> createResponseEntity(String reason) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", reason);
        return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private <T> ResponseEntity<T> createResponseEntity2(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String[] msg = result.getAllErrors().stream().map(ObjectError::toString).toArray(String[]::new);
        return createResponseEntity2(getDefaultBody(request, ErrorAttributeOptions.defaults(), msg), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> getDefaultBody(WebRequest request, ErrorAttributeOptions options, String... msg) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg.length != 0) {
            body.put("message", msg);
        }
        return body;
    }
}
