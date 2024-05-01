package ru.tsu.hits.hitsinternship.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.tsu.hits.hitsinternship.dto.api.ApiError;
import ru.tsu.hits.hitsinternship.exception.ConflictException;
import ru.tsu.hits.hitsinternship.exception.NotFoundException;
import ru.tsu.hits.hitsinternship.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(HttpServletRequest request,
                                                              MethodArgumentNotValidException exception) {
        logException(request, exception);

        var errors = new HashMap<String, List<String>>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();

                    if (errors.containsKey(fieldName)) {
                        var messages = errors.get(fieldName);
                        messages.add(errorMessage);
                    } else {
                        var messages = new ArrayList<String>();
                        messages.add(errorMessage);
                        errors.put(fieldName, messages);
                    }
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("Validation error", errors));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException exception)
            throws NoResourceFoundException {
        throw exception;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(HttpServletRequest request,
                                                              BadRequestException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request,
                                                            NotFoundException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request,
                                                                UnauthorizedException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(HttpServletRequest request,
                                                                AccessDeniedException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(HttpServletRequest request,
                                                            ConflictException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request,
                                                    Exception exception
    ) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Internal service error"));
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

}
