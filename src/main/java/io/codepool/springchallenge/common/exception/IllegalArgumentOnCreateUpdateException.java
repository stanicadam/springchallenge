package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class IllegalArgumentOnCreateUpdateException extends RuntimeException{
    public IllegalArgumentOnCreateUpdateException(String message){
        super(message);
    }
}