package com.messenger.chatty.exception;

import com.messenger.chatty.exception.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;





@RestControllerAdvice
public class ControllerExceptionHandler {


    // it will be called when NOT Authenticated member is caught at controller level
    // it is a NOT EXPECTED!!
    @ExceptionHandler(UnexpectedNotAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedNotAuthenticatedException(HttpServletRequest request, UnexpectedNotAuthenticationException ex) {
        return getResEntityWithBadRequest(request.getRequestURI(),ErrorDetail.UNEXPECTED_NOT_AUTHENTICATION, ex.getMessage());

    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(HttpServletRequest request,NoSuchElementException ex) {
        return getResEntityWithBadRequest(request.getRequestURI(),ErrorDetail.NO_SUCH_ELEMENT, ex.getMessage());
    }


    @ExceptionHandler({ConstraintViolationException.class, InvalidRequestParamException.class })
    public ResponseEntity<ErrorResponse> handleRequestBodyValidationExceptions(HttpServletRequest request, RuntimeException ex) {

        return getResEntityWithBadRequest(request.getRequestURI(), ErrorDetail.INVALID_REQUEST_BODY, ex.getMessage());

    }

    @ExceptionHandler(DuplicatedNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(HttpServletRequest request, DuplicatedNameException ex) {
        return getResEntityWithBadRequest(request.getRequestURI(), ErrorDetail.DUPLICATED_NAME, ex.getMessage());
    }
    @ExceptionHandler({InvalidInvitationCodeException.class})
    public ResponseEntity<ErrorResponse> handleInvalidInvitationException(HttpServletRequest request, InvalidInvitationCodeException ex) {
        return getResEntityWithBadRequest(request.getRequestURI() ,ErrorDetail.INVALID_INVITE_CODE, ex.getMessage()) ;
    }
    @ExceptionHandler(PasswordInEqualityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSignupRequest(HttpServletRequest request, PasswordInEqualityException ex) {
        return getResEntityWithBadRequest(request.getRequestURI(),ErrorDetail.PASSWORD_INEQUALITY, ex.getMessage());
    }





    private ResponseEntity<ErrorResponse> getResEntityWithBadRequest(String errorPath, ErrorDetail detail, String message){
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(errorPath,HttpStatus.BAD_REQUEST
                        ,detail
                        ,message ));

    }


}
