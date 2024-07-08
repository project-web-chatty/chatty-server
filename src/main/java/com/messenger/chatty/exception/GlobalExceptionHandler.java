package com.messenger.chatty.exception;

import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import com.messenger.chatty.exception.custom.UnexpectedNotAuthenticatedException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;





@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnexpectedNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedNotAuthenticatedException(UnexpectedNotAuthenticatedException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorType.UNEXPECTED_NOT_AUTHENTICATION,ex.getMessage()));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorType.NO_SUCH_ELEMENT,ex.getMessage()));
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorType.INVALID_REQUEST_BODY,ex.getMessage()));

    }

    @ExceptionHandler({DuplicateUsernameException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorType.DUPLICATED_USERNAME,"duplicated username"));
    }

}
