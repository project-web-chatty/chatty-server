package com.messenger.chatty.exception;

import com.messenger.chatty.dto.response.ErrorResponseDTO;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("handling -------");
        // customize it later
        /*Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });*/

        return ResponseEntity.badRequest().body(ErrorResponseDTO.from("invalid request body"));


    }

    @ExceptionHandler({DuplicateUsernameException.class})
    public ResponseEntity<ErrorResponseDTO> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        System.out.println("handling -------");
        return ResponseEntity.badRequest().body(ErrorResponseDTO.from("duplicated username"));
    }
}
