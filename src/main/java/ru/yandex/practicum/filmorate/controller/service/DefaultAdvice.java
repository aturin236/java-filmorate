package ru.yandex.practicum.filmorate.controller.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.model.service.ValidationException;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleException(ValidationException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
