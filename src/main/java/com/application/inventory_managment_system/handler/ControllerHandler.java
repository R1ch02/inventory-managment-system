package com.application.inventory_managment_system.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.application.inventory_managment_system.exceptions.ApiServiceException;
import com.application.inventory_managment_system.model.dto.response.MessageResponse;

@Slf4j
@RestControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(ApiServiceException.class)
    public final ResponseEntity<MessageResponse> handleApiServiceException(ApiServiceException e) {
        log.debug("Api Service exception: {}", e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatusCode())
                .body(new MessageResponse("Error", e.getMessage()));
    }


    //Посмотрпеть реализацию
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> errorMap.put(((FieldError) error).getField(), error.getDefaultMessage()));
        
        log.debug("Method argument not valid error: {}", e.getAllErrors().stream().map(error -> "\n%s - %s".formatted(((FieldError) error).getField(), error.getDefaultMessage())).collect(Collectors.joining("; ")));
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", errorMap));
    }


}
