package com.gossip.arrienda_tu_finca.exceptions;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RentalRequestNotFoundException.class)
    public ResponseEntity<String> handleRentalRequestNotFoundException(RentalRequestNotFoundException ex) {
        logger.error("Rental request not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
 
    @ExceptionHandler(InvalidAmountOfResidentsException.class)
    public ResponseEntity<String> handleInvalidAmountOfResidentsException(InvalidAmountOfResidentsException ex) {
        logger.error("Invalid amount of residents: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
        logger.error("Invalid date: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<String> handleInvalidPaymentException(InvalidPaymentException ex) {
        logger.error("Invalid payment: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReviewException.class)
    public ResponseEntity<String> handleInvalidReviewException(InvalidReviewException ex) {
        logger.error("Invalid review: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<String> handlePropertyNotFoundException(PropertyNotFoundException ex) {
        logger.error("Property not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<ErrorMessage> handleUserNotValidException(UserNotValidException e) {
        logger.error("User information is not compliant with the requirements: {}", e.getMessage());
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
        logger.error("User not found: {}", e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(
            "User not found: " + e.getMessage(), 
            Calendar.getInstance().getTime(), 
            HttpStatus.NOT_FOUND.value(), 
            HttpStatus.NOT_FOUND.getReasonPhrase());
        
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(errorMessage, responseHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handleImageNotFoundException(ImageNotFoundException ex) {
        logger.error("Image not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
