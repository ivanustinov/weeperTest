package ru.ustinov.sapertest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.ustinov.sapertest.exception.SaperException;
import ru.ustinov.sapertest.to.ErrorResopnse;

import java.util.*;

/**
 * Обработка ошибок валидации и выбрасываемых исключений
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 11.02.2024
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class SaperExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TURN_ROW_ERROR = "valid.turn_row.message";
    public static final String TURN_COL_ERROR = "valid.turn_col.message";
    public static final String TURN_CELL_OPENED = "valid.turn_cell_opened.message";
    public static final String NEW_GAME_MINE_COUNT_CHECK = "valid.mine_count.message";
    public static final String SESSION_EXPIRED = "session_expired.message";
    public static final String INPUT_DATA_MISTAKE = "input_data_parsing_mistake.message";

    @Autowired
    private final MessageSourceAccessor messageSourceAccessor;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers
        , HttpStatusCode status, WebRequest request) {
        final String mistake = Objects.requireNonNull(ex.getMessage());
        final String message = messageSourceAccessor.getMessage(INPUT_DATA_MISTAKE);
        final String join = String.join(" ", message, mistake);
        final ErrorResopnse errorResopnse = new ErrorResopnse(join);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResopnse);
    }

    @ExceptionHandler(SaperException.class)
    public ResponseEntity<?> saperException(SaperException ex) {
        log.error("SessionExpieredException ", ex);
        final String messagCode = Objects.requireNonNull(ex.getReason());
        final String message = messageSourceAccessor.getMessage(messagCode, ex.getParams());
        final ErrorResopnse errorResopnse = new ErrorResopnse(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResopnse);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result) {
        final Optional<ObjectError> first = result.getAllErrors().stream().findFirst();
        String message = "";
        if (first.isPresent()) {
             message = messageSourceAccessor.getMessage(first.get());
        }
        final ErrorResopnse errorResopnse = new ErrorResopnse(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResopnse);
    }

}
