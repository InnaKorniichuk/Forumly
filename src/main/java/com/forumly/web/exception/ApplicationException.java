package com.forumly.web.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException{
    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, HttpStatus status){
        super(message);
    }

    public static ApplicationException notFound(String message) {

        return new ApplicationException(message);
    }

    public static ApplicationException conflict(String message) {

        return new ApplicationException(message);
    }

    public static ApplicationException unauthorized(String message){
        return new ApplicationException(message);
    }

    public static ApplicationException badRequest(String message) {
        return new ApplicationException(message, HttpStatus.BAD_REQUEST);
    }
}
