package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The custom Entity already exists exception.
 * Used to let the end user know they are failing a unique constraint check.
 */
@ResponseStatus(value= HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends RuntimeException{
    public EntityAlreadyExistsException(String entityType, String identifier){
        super(String.format("%s with this %s already exists", entityType, identifier));
    }
}