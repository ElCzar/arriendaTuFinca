package com.gossip.arrienda_tu_finca.exceptions;

import java.util.Calendar;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<String> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        // Retorna 404 Not Found con el mensaje de la excepción
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<ErrorMessage> handleUserNotValidException(UserNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage(
            "User information is not compliant with the requirements: " + e.getMessage(), 
            Calendar.getInstance().getTime(), 
            HttpStatus.BAD_REQUEST.value(), 
            HttpStatus.BAD_REQUEST.getReasonPhrase());
        
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(errorMessage, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(
            "User not found: " + e.getMessage(), 
            Calendar.getInstance().getTime(), 
            HttpStatus.NOT_FOUND.value(), 
            HttpStatus.NOT_FOUND.getReasonPhrase());
        
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(errorMessage, responseHeaders, HttpStatus.NOT_FOUND);
    }
}
