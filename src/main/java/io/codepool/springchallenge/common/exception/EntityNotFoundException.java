package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The custom Entity not found exception.
 * Thrown when querying for an entity that does not exist.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entityType, Object criteria){
        super(String.format("Could not find entity of type %s with identifier %s", entityType, criteria));
    }
}

