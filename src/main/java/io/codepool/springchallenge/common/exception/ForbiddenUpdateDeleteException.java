package io.codepool.springchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The custom Forbidden deletion exception.
 * This exception is thrown when a user tries to delete or update an entity that does not belong to them.
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class ForbiddenUpdateDeleteException extends RuntimeException{
    public ForbiddenUpdateDeleteException(String entityType){
        super("You are not the owner of this " + entityType);
    }
}