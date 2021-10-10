package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//could argue weather 400 or 422 belong here,
//not a big mistake whatever we choose
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class IllegalArgumentOnCreateUpdateException extends RuntimeException{
    public IllegalArgumentOnCreateUpdateException(String message){
        super(message);
    }
}