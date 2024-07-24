package com.messenger.chatty.presentation;
import com.messenger.chatty.presentation.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalWithoutReason
                (e,  ErrorStatus.COMMON_INVALID_ARGUMENT,HttpHeaders.EMPTY, request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleDBConstraintViolationEx(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst().get();

        return handleExceptionInternalWithoutReason
                (e, ErrorStatus.COMMON_CONSTRAINT_VIOLATION, HttpHeaders.EMPTY, request,errorMessage);
    }


    @ExceptionHandler
    public ResponseEntity<Object> handleGeneralEx(GeneralException generalException, HttpServletRequest request) {
        Reason reason = generalException.getErrorReason();
        return handleExceptionInternalWithReason(generalException, reason, null, request);
    }
    @ExceptionHandler
    public ResponseEntity<Object> handleUnExpectedEx(Exception e, WebRequest request) {
        e.printStackTrace();
        return handleExceptionInternalWithoutReason
                (e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, request, e.getMessage());
    }









    private ResponseEntity<Object> handleExceptionInternalWithReason(Exception e, Reason reason,
                                                                     HttpHeaders headers, HttpServletRequest request) {
        return super.handleExceptionInternal(
                e,
                ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null),
                headers,
                reason.getHttpStatus(),
                new ServletWebRequest(request)
        );
    }
    private ResponseEntity<Object> handleExceptionInternalWithoutReason(Exception e, ErrorStatus errorStatus,
                                                                        HttpHeaders headers, WebRequest request,
                                                                        Object errorDetail) {
        return super.handleExceptionInternal(
                e,
                ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), errorDetail),
                headers,
                errorStatus.getHttpStatus(),
                request
        );
    }


}
