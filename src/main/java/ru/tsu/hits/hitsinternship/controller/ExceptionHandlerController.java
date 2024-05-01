package ru.tsu.hits.hitsinternship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tsu.hits.hitsinternship.exception.ApiError;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {

        Map<String, List<String>> errors = new HashMap<>();

        exception
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    if (message != null) {
                        if (errors.containsKey(fieldName)) {
                            errors.get(fieldName).add(message);
                        } else {
                            List<String> newErrorList = new ArrayList<>();
                            newErrorList.add(message);

                            errors.put(fieldName, newErrorList);
                        }
                    }
                });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

}
