package com.application.exceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class ApiServiceException extends RuntimeException {

    private HttpStatusCode httpStatusCode;

    public ApiServiceException(String message, HttpStatusCode httpStatus){
        super(message);
        this.httpStatusCode = httpStatus;
    }
}
