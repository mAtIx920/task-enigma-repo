package org.rest.restapp.controllers.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(ErrorType.RESOURCE_ALREADY_EXISTS.getStatus())
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(ErrorType.RESOURCE_ALREADY_EXISTS.getStatus())
                        .timestamp(formatter.format(java.time.LocalDateTime.now()))
                        .errorType(ErrorType.RESOURCE_ALREADY_EXISTS)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<HashMap<String, String>> errorList = exception.getFieldErrors().stream()
                .map(errorField -> {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put(errorField.getField(), errorField.getDefaultMessage());
                    return errors;
                }).toList();

        return ResponseEntity.status(ErrorType.BAD_REQUEST.getStatus())
                .body(ErrorResponse.builder()
                        .message(errorList.toString())
                        .status(ErrorType.BAD_REQUEST.getStatus())
                        .timestamp(formatter.format(java.time.LocalDateTime.now()))
                        .errorType(ErrorType.BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(ErrorType.RESOURCE_NOT_FOUND.getStatus())
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(ErrorType.RESOURCE_NOT_FOUND.getStatus())
                        .timestamp(formatter.format(java.time.LocalDateTime.now()))
                        .errorType(ErrorType.RESOURCE_NOT_FOUND)
                        .build());
    }
}
