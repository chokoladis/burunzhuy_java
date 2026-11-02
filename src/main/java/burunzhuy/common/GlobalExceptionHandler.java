package burunzhuy.common;

import burunzhuy.tool.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {

        Logger.logToFile("global_errors.log", ex.getClass().getName() + ": " + ex.getMessage());

        Map<String, List> errors = new HashMap<>();
        List<String> errorsMessages = new ArrayList<>();
        HttpStatus status  = HttpStatus.NOT_FOUND;

        if (ex.getClass() == NoResourceFoundException.class) {
            errorsMessages.add("Данный ресурс не был найден");
            errors.put("error", errorsMessages);
        } else {
            errorsMessages.add("Произошла не предвиденная ошибка");
            errors.put("errors", errorsMessages);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity
                .status(status)
                .body(errors);
    }
}