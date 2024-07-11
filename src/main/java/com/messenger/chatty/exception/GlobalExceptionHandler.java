package com.messenger.chatty.exception;

import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.exception.custom.InvalidInvitationCodeException;
import com.messenger.chatty.exception.custom.PasswordInEqualityException;
import com.messenger.chatty.exception.custom.UnexpectedNotAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;





@RestControllerAdvice
public class GlobalExceptionHandler {


    // it will be called when NOT Authenticated member is caught at controller level
    // it is a NOT EXPECTED!!
    @ExceptionHandler(UnexpectedNotAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedNotAuthenticatedException(HttpServletRequest request, UnexpectedNotAuthenticationException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request, HttpStatus.BAD_REQUEST,
                                ErrorDetail.UNEXPECTED_NOT_AUTHENTICATION,
                                ex.getMessage()));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(HttpServletRequest request,NoSuchElementException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request,HttpStatus.BAD_REQUEST
                        ,ErrorDetail.NO_SUCH_ELEMENT
                , ex.getMessage()));
    }


    @ExceptionHandler({ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleRequestBodyValidationExceptions(HttpServletRequest request, RuntimeException ex) {

        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request,HttpStatus.BAD_REQUEST
                        ,ErrorDetail.INVALID_REQUEST_BODY
                        , ex.getMessage()));

    }

    @ExceptionHandler({DuplicatedNameException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(HttpServletRequest request, DuplicatedNameException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request,HttpStatus.BAD_REQUEST
                ,ErrorDetail.DUPLICATED_NAME
                        ,ex.getMessage() ));
    }
    @ExceptionHandler({InvalidInvitationCodeException.class})
    public ResponseEntity<ErrorResponse> handleInvalidInvitationException(HttpServletRequest request, InvalidInvitationCodeException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request,HttpStatus.BAD_REQUEST
                        ,ErrorDetail.INVALID_CODE
                        ,ex.getMessage() ));
    }
    @ExceptionHandler({PasswordInEqualityException.class})
    public ResponseEntity<ErrorResponse> handleInvalidSignupRequest(HttpServletRequest request, PasswordInEqualityException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(request,HttpStatus.BAD_REQUEST
                        ,ErrorDetail.PASSWORD_INEQUALITY
                        ,ex.getMessage() ));
    }


}
