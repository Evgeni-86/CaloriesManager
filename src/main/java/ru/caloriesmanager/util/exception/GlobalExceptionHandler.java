package ru.caloriesmanager.util.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<JsonIncorrect> handlerException(NotFoundException notFoundException) {
        JsonIncorrect jsonIncorrect = new JsonIncorrect();
        jsonIncorrect.setInfo(notFoundException.getMessage());
        return new ResponseEntity<>(jsonIncorrect, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<JsonIncorrect> handlerException(Exception exception) {
        JsonIncorrect jsonIncorrect = new JsonIncorrect();
        jsonIncorrect.setInfo(exception.getMessage());
        return new ResponseEntity<>(jsonIncorrect, HttpStatus.BAD_REQUEST);
    }
}
