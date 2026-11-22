package com.forumly.web.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
    private final HttpStatus status;

    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static ApplicationException notFound(String message) {
        return new ApplicationException(message, HttpStatus.NOT_FOUND);
    }

    public static ApplicationException conflict(String message) {
        return new ApplicationException(message, HttpStatus.CONFLICT);
    }

    public static ApplicationException unauthorized(String message) {
        return new ApplicationException(message, HttpStatus.FORBIDDEN);
    }

    public static ApplicationException badRequest(String message) {
        return new ApplicationException(message, HttpStatus.BAD_REQUEST);
    }
}
