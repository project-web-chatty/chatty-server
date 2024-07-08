package com.messenger.chatty;

import com.messenger.chatty.dto.ErrorResponseDTO;
import com.messenger.chatty.exception.custom.DuplicateUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;


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
