package com.joshfullstack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidUpdateException extends RuntimeException{
    public InvalidUpdateException(String message) {
        super(message);
    }
}
