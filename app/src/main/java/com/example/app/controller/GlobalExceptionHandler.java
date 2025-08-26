package com.example.app.controller;

import com.example.app.dto.ErrorResponse;
import com.example.app.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validaçao do @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorResponse(err.getDefaultMessage(), err.getField()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    //Captura exceçoes de entidade nao encontrada
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleRuntime(ProductNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), null));
    }

    //Captura erro de caracter inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex){
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse( "Please insert a valid character.", null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex){
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage(), null));
    }

    //Captura erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error", null));
    }
}
